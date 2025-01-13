import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const Products = () => {
  const { id } = useParams();
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pageCount, setPageCount] = useState(1);
  const [label, setLabel] = useState(new Set());
  const [minPrice, setMinPrice] = useState(-1);
  const [maxPrice, setMaxPrice] = useState(-1);
  const [selectedPrice, setSelectedPrice] = useState([0, 0]);

  const [nav, setNav] = useState(true);

  const handleNav = () =>{
    setNav((prevNav) => !prevNav);
  }

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      try {
        const endpoint = id
          ? `http://localhost:8080/api/product/${id}/${page}`
          : `http://localhost:8080/api/product/page?page=${page}`;
        const response = await axios.get(endpoint, { withCredentials: true });

        const allProductsEndpoint = id
          ? `http://localhost:8080/api/product/${id}`
          : `http://localhost:8080/api/product`;
        const allProductsResponse = await axios.get(allProductsEndpoint, { withCredentials: true });

        const pagesCnt = Math.ceil(allProductsResponse.data.length / 4);
        setPageCount(pagesCnt);
        setProducts(response.data);

        const uniqueLabels = new Set();
        let min = Infinity;
        let max = -Infinity;

        allProductsResponse.data.forEach((product) => {
          const price = (product.mrp * (1 - product.discount / 100)).toFixed(2);
          min = Math.min(min, price);
          max = Math.max(max, price);

          if (product.label) {
            uniqueLabels.add(product.label);
          }
        });

        setMinPrice(min);
        setMaxPrice(max + 1);
        setSelectedPrice([min, max + 1]);
        setLabel(uniqueLabels);
      } catch (err) {
        setError("Failed to fetch products. Please try again.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [id, page]);

  if (loading) {
    return <div className="text-center text-gray-500 text-lg animate-pulse">Loading products...</div>;
  }

  if (error) {
    return <div className="text-center text-red-500 text-lg">{error}</div>;
  }

  const handlePriceChange = (e) => {
    const value = +e.target.value;
    setSelectedPrice([minPrice, value]);
  };

  const filteredProducts = products.filter((product) => {
    const discountedPrice = (product.mrp * (1 - product.discount / 100)).toFixed(2);
    return discountedPrice >= selectedPrice[0] && discountedPrice <= selectedPrice[1];
  });

  return (
    <div className="p-4">
      <h1 className="text-2xl font-semibold text-center mb-6 text-gray-800">
        {products[0]?.category?.categoryName || "Products"}
      </h1>
      <div className="filter cursor-pointer">
        <span onClick={handleNav} className="material-symbols-outlined">menu</span>
      </div>
      <div className="flex gap-8">
        <div className={nav ? "bg-[#fff] rounded-lg border border-solid border-[#ccc] px-4 py-4 filter": "hidden"}>
          <div className="font-bold py-2">Label</div>
          {[...label].map((labelValue, idx) => (
            <div key={idx}>
              <input type="checkbox" id={`label-${idx}`} value={labelValue} />
              <label className="px-1" htmlFor={`label-${idx}`}>{labelValue}</label>
            </div>
          ))}

          <div className="Price font-bold py-2">Price</div>
          <div className="flex items-center gap-4">
            <span className="text-[12px]">₹{selectedPrice[0]}</span>
            <input
              type="range"
              min={minPrice}
              max={maxPrice}
              onChange={handlePriceChange}
              value={selectedPrice[1]}
              name="price"
              id="price"
            />
            <span className="text-[12px]">₹{selectedPrice[1]}</span>
          </div>
        </div>

        <div className="flex px-8 gap-4 flex-wrap">
          {filteredProducts.length > 0 ? (
            filteredProducts.map((product) => {
              const discountedPrice = (product.mrp * (1 - product.discount / 100)).toFixed(2);
              return (
                <div
                  key={product.id}
                  className="border w-[250px] bg-white rounded-lg shadow-sm overflow-hidden text-sm"
                >
                  <img
                    src={`/src/assets/Images/${product.category.categoryName}.png`}
                    alt={product.name}
                    className="w-full "
                  />
                  <div className="p-2">
                    <h2 className="font-medium mb-1 truncate">{product.name}</h2>
                    <p className="text-gray-600 mb-2 truncate">{product.description}</p>
                    <div className="text-gray-800 mb-2">
                      <span className="line-through text-gray-500 mr-2">₹{product.mrp}</span>
                      <span className="font-bold text-green-600">₹{discountedPrice}</span>
                    </div>
                  </div>
                </div>
              );
            })
          ) : (
            <div className="text-gray-600 text-center col-span-full">
              No products found in this category.
            </div>
          )}
        </div>
      </div>

      <div className="flex justify-center mt-6">
        {Array.from({ length: pageCount }, (_, i) => (
          <button
            key={i}
            onClick={() => setPage(i + 1)}
            className={`px-4 py-2 mx-1 border rounded ${
              page === i + 1 ? "bg-blue-600 text-white" : "bg-gray-200"
            }`}
          >
            {i + 1}
          </button>
        ))}
      </div>
    </div>
  );
};

export default Products;
