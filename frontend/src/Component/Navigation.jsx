import axios from "axios";
import { useState } from "react";
import { useAuth } from "../Context/AuthContext";
import { useNavigate } from "react-router-dom";

const Navigation = () => {
  const { userData, setUserData } = useAuth(); 
  const [displayInfo, setDisplayInfo] = useState(false);

  const displayInfoHandler = () => {
    setDisplayInfo((prev) => !prev);
  };

  const navigate = useNavigate();

  console.log(userData);


  const homePage = () => {
    navigate("/dashboard");
  }

  const logoutHandler = async () => {
    try {
      const response = await axios.post("http://localhost:8080/logout", {}, { withCredentials: true });
      console.log("Logout successful:", response.data);
      setUserData(null);
      navigate("/")
      window.location.reload(); 
    } catch (e) {
      console.error("Error occurred during logout:", e);
    }
  };

  return (
    <div className="bg-black text-white flex justify-between px-[5%] py-2 items-center">
      <div className="left">
        <h1 className="text-2xl font-bold cursor-pointer" onClick={homePage}>ECOMMERCE</h1>
      </div>
      <div className="right relative">
        {userData ? (
          <div className="relative">
            <div
              onClick={displayInfoHandler}
              className="border border-solid border-gray-500 rounded-full w-[40px] h-[40px] cursor-pointer overflow-hidden"
            >
              {userData.picture && (
                <img
                  src={userData.picture}
                  className="rounded-full object-cover w-full h-full"
                  alt="User"
                  referrerPolicy="no-referrer"
                />
              )}
            </div>
            {displayInfo && (
              <div className="absolute px-8 right-0 mt-2 bg-white text-black rounded-lg shadow-lg p-4">
                <p className="font-bold text-center">{userData.name}</p>
                <p className="text-sm text-gray-600 text-center">{userData.email}</p>
                <div
                  onClick={logoutHandler}
                  className="text-center text-white my-2 rounded-lg py-1 cursor-pointer bg-blue-600 border border-solid"
                >
                  Logout
                </div>
              </div>
            )}
          </div>
        ) : (
          <div className="text-white font-medium cursor-pointer">
            <a href="http://localhost:8080/oauth2/authorization/google">Login</a>
          </div>
        )}
      </div>
    </div>
  );
};

export default Navigation;
