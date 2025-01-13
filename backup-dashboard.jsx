import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const [categories, setCategories] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);

  const navigate = useNavigate();

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
        const totalCategory = await axios.get(`http://localhost:8080/api/category`, {
          withCredentials: true,
        });

        setTotalPage(Math.ceil(totalCategory.data.length / 3)); 
        console.log(Math.ceil(totalCategory.data.length / 3));

        const response = await axios.get(`http://localhost:8080/api/category/page`, {
          params: { page, limit: 3 },
          withCredentials: true,
        });
        setCategories(response.data);
        console.log(response.data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, [page]);

  const handlePageChange = (direction) => {
    if (direction === "next" && page < totalPage) {
      setPage((prevPage) => prevPage + 1);
    } else if (direction === "prev" && page > 1) {
      setPage((prevPage) => prevPage - 1);
    }
  };

  return (
    <div className="bg-gray-100 min-h-screen p-6">
      <h1 className="text-4xl font-bold text-center mb-8 text-gray-800">Categories</h1>
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

      <div className="flex justify-center mt-8 gap-4">
        <button
          onClick={() => handlePageChange("prev")}
          disabled={page === 1}
          className="bg-gray-300 p-2 rounded-md"
        >
          Previous
        </button>
        <span className="text-lg font-semibold">{`Page ${page} of ${totalPage}`}</span>
        <button
          onClick={() => handlePageChange("next")}
          disabled={page === totalPage}
          className="bg-gray-300 p-2 rounded-md"
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default Dashboard;
