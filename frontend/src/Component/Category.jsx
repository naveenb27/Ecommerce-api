import axios from "axios";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const Category = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const page = queryParams.get('page');  // Get page from the query string

  const [categories, setCategories] = useState([]);

  const handleClick = (id) => {
    if (id === 0) {
      navigate(`/products`);
    } else {
      navigate(`/products/${id}`);
    }
  };

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/category/page`, {
          params: { page, limit: 3 },
          withCredentials: true,
        });
        setCategories(response.data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, [page]);

  return (
    <div className="bg-gray-100">
      <h1 className="text-4xl font-bold text-center py-6 text-gray-800">Categories</h1>
      <div className="flex flex-wrap justify-center cursor-pointer gap-8">
        {categories.map((obj, index) => (
          <div
            className="bg-white shadow-lg rounded-lg overflow-hidden transform transition hover:scale-105 hover:shadow-xl"
            key={index}
            onClick={() => handleClick(obj.categoryID)}
          >
            <img
              className="w-[270px] h-[250px]"
              src={`/src/assets/Images/${obj.categoryName}.png`}
              alt={obj.categoryName}
            />
            <div className="p-4">
              <h2 className="text-xl font-semibold text-gray-700 text-center">
                {obj.categoryName}
              </h2>
            </div>
          </div>
        ))}
        <div
          className="bg-white shadow-lg rounded-lg overflow-hidden transform transition hover:scale-105 hover:shadow-xl"
          key={4}
          onClick={() => handleClick(0)}
        >
          <img
            className="w-[270px] h-[250px]"
            src={`/src/assets/Images/product.jpg`}
            alt="all products"
          />
          <div className="p-4">
            <h2 className="text-xl font-semibold text-gray-700 text-center">All products</h2>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Category;
