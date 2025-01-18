import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const SearchResult = () => {
  const [products, setProducts] = useState([]);
  // const [loading, setLoading] = useState(true);
  // const [error, setError] = useState(null);
  const [label, setLabel] = useState(new Set());
  const [minPrice, setMinPrice] = useState(-1);
  const [maxPrice, setMaxPrice] = useState(-1);
  const [selectedPrice, setSelectedPrice] = useState([0, 0]);
  const [selectedLabels, setSelectedLabels] = useState([]);
  
  const [nav, setNav] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [productsPerPage] = useState(6); 
  
  const handleNav = () => {
    setNav((prevNav) => !prevNav);
  };

  const BACKENDURL = import.meta.env.VITE_BACKEND_URL;


  const { search } = useParams();
  console.log(search);

  useEffect(() => {
    const handleSearch = async () => {
      // setLoading(true);
      try {
        const response = await axios.get(
          `${BACKENDURL}/api/elastic/products/search?search=${search}`,
          { withCredentials: true }
        );
        setProducts(response.data);
        console.log(response.data);

        const uniqueLabels = new Set();
        let min = Infinity;
        let max = -Infinity;

        response.data.forEach((product) => {
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
      } catch (e) {
        // setError("Failed to fetch products. Please try again.");
        setProducts([])
        console.log(e);
      }
    };

    handleSearch();
  }, [search]);

  const handleLabels = (e) => {
    const value = e.target.value;
    const isChecked = e.target.checked;

    if (isChecked) {
      setSelectedLabels((prev) => [...prev, value]);
    } else {
      setSelectedLabels((prev) => prev.filter((label) => label !== value));
    }
  };

  const handlePriceChange = (e) => {
    const value = +e.target.value;
    setSelectedPrice([minPrice, value]);
  };

  const filteredProducts = products.filter((product) => {
    const discountedPrice = (product.mrp * (1 - product.discount / 100)).toFixed(2);
    const isInPriceRange = discountedPrice >= selectedPrice[0] && discountedPrice <= selectedPrice[1];
    const hasSelectedLabel = selectedLabels.length === 0 || selectedLabels.includes(product.label);
    return isInPriceRange && hasSelectedLabel;
  });

  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = filteredProducts.slice(indexOfFirstProduct, indexOfLastProduct);

  const totalPages = Math.ceil(filteredProducts.length / productsPerPage);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

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
              <input type="checkbox" onChange={handleLabels} id={`label-${idx}`} value={labelValue} />
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
          {currentProducts.length > 0 ? (
            currentProducts.map((product) => {
              const discountedPrice = (product.mrp * (1 - product.discount / 100)).toFixed(2);
              return (
                <div
                  key={product.id}
                  className="border w-[250px] bg-white rounded-lg shadow-sm overflow-hidden text-sm"
                >
                  <img
                    src={`/src/assets/Images/${product.category}.png`}
                    alt={product.name}
                    className="w-full"
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

      <div className="flex justify-center gap-4 mt-6">
        <button
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
          className="bg-gray-300 p-2 rounded-md"
        >
          Previous
        </button>
        <span className="text-lg font-semibold">{`Page ${currentPage} of ${totalPages}`}</span>
        <button
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
          className="bg-gray-300 p-2 rounded-md"
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default SearchResult;
