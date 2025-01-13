import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  // const [categories, setCategories] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const totalCategory = await axios.get(`http://localhost:8080/api/category`, {
          withCredentials: true,
        });

        setTotalPage(Math.ceil(totalCategory.data.length / 3));

        navigate(`/category?page=${page}`); 
        // setCategories(response.data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, [page]);

  const handlePageChange = (direction) => {
    let newPage = page;
    if (direction === "next" && page < totalPage) {
      newPage = page + 1;
    } else if (direction === "prev" && page > 1) {
      newPage = page - 1;
    }
    setPage(newPage);
    navigate(`/category?page=${newPage}`);  // Corrected URL format
  };

  return (
    <div className="bg-gray-100 min-h-[100px] p-6">
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
