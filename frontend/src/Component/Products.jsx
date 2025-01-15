import axios from "axios";
import { useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";

const Products = () => {
  const { id } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [allProducts, setAllProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pageCount, setPageCount] = useState(1);
  const [label, setLabel] = useState(new Set());
  const [selectedLabels, setSelectedLabels] = useState(new Set());
  const [minPrice, setMinPrice] = useState(-1);
  const [maxPrice, setMaxPrice] = useState(-1);
  const [selectedPrice, setSelectedPrice] = useState([0, 0]);
  const [nav, setNav] = useState(true);

  const page = parseInt(searchParams.get("page")) || 1;

  const handleNav = () => {
    setNav((prevNav) => !prevNav);
  };

  const handleLabelChange = (e) => {
    const value = e.target.value;
    setSelectedLabels((prev) =>
      prev.has(value) ? new Set([...prev].filter((item) => item !== value)) : new Set([...prev, value])
    );
  };

  const handlePriceChange = (e) => {
    const value = +e.target.value;
    setSelectedPrice([selectedPrice[0], value]);
  };

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

        const pagesCnt = Math.ceil(allProductsResponse.data.length / 6);
        setPageCount(pagesCnt);
        setProducts(response.data);
        setAllProducts(allProductsResponse.data);

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

  const filterProduct = () => {
    setLoading(true);
    try {
      const filteredProducts = allProducts.filter((product) => {
        const discountedPrice = product.mrp * (1 - product.discount / 100);
        if (discountedPrice < selectedPrice[0] || discountedPrice > selectedPrice[1]) {
          return false;
        }

        if (selectedLabels.size > 0 && !selectedLabels.has(product.label)) {
          return false;
        }

        return true;
      });

      setProducts(filteredProducts);
      setPageCount(Math.ceil(filteredProducts.length / 4));
    } catch (e) {
      setError("Failed to apply filters. Please try again.");
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const getPaginationRange = () => {
    const range = 5;
    let start = Math.max(1, page - Math.floor(range / 2));
    let end = Math.min(pageCount, start + range - 1);

    if (end - start < range - 1) {
      start = Math.max(1, end - range + 1);
    }

    return { start, end };
  };

  if (loading) {
    return <div className="text-center text-gray-500 text-lg animate-pulse">Loading products...</div>;
  }

  if (error) {
    return <div className="text-center text-red-500 text-lg">{error}</div>;
  }

  const { start, end } = getPaginationRange();

  return (
    <div className="p-4">
      <h1 className="text-2xl font-semibold text-center mb-6 text-gray-800">
        {products[0]?.category?.categoryName || "Products"}
      </h1>
      <div className="filter cursor-pointer">
        <span onClick={handleNav} className="material-symbols-outlined">menu</span>
      </div>
      <div className="flex gap-8">
        <div className={nav ? "bg-[#fff] rounded-lg border border-solid border-[#ccc] px-4 py-4 filter" : "hidden"}>
          <div className="font-bold py-2">Label</div>
          {[...label].map((labelValue, idx) => (
            <div key={idx}>
              <input
                type="checkbox"
                id={`label-${idx}`}
                value={labelValue}
                onChange={handleLabelChange}
              />
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

          <button className="filter" onClick={filterProduct}>
            Go
          </button>
        </div>

        <div className="flex px-8 gap-4 flex-wrap">
          {products.length > 0 ? (
            products.map((product) => {
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
        <button
          onClick={() => setSearchParams({ page: 1 })}
          className="px-4 py-2 mx-1 border rounded bg-gray-200"
        >
          First
        </button>

        {start > 1 && (
          <span className="px-2 text-gray-500">...</span>
        )}

        {Array.from({ length: end - start + 1 }, (_, i) => (
          <button
            key={i}
            onClick={() => setSearchParams({ page: start + i })}
            className={`px-4 py-2 mx-1 border rounded ${
              page === start + i ? "bg-blue-600 text-white" : "bg-gray-200"
            }`}
          >
            {start + i}
          </button>
        ))}

        {end < pageCount && (
          <span className="px-2 text-gray-500">...</span>
        )}

        <button
          onClick={() => setSearchParams({ page: pageCount })}
          className="px-4 py-2 mx-1 border rounded bg-gray-200"
        >
          Last
        </button>
      </div>
    </div>
  );
};

export default Products;
