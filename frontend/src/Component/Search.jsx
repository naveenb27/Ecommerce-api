
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Search = () => {
    const [search, setSearch] = useState("");
    
    const navigate = useNavigate();
    const handleInput = (e) => {
        setSearch(e.target.value);
    };

   const handleSearch = () => {
        if(search.trim() !== ""){
            navigate(`/search/${search}`);
        }

   } 
    return (
        <div className="flex justify-around items-center shadow-lg py-2 px-4 gap-4">
            <div className="text-[15px]">
                Get up to 50% off today. Don't miss this opportunity!
            </div>
            <div className="flex items-center gap-3 max-w-md">
                <input
                    value={search}
                    onChange={handleInput}
                    type="text"
                    placeholder="Search product"
                    className="border px-2 rounded-lg border-solid py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <span
                    onClick={handleSearch}
                    className="material-symbols-outlined border border-solid p-2 rounded-lg cursor-pointer bg-blue-500 shadow-sm text-white"
                >
                    search
                </span>
            </div>
            
        </div>
    );
};

export default Search;
