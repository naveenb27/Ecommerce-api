import axios from "axios";
import { createContext, useContext, useEffect, useState } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  console.log(import.meta.env)

  const BACKENDURL = import.meta.env.VITE_BACKEND_URL;

  console.log(BACKENDURL);
  
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getUser = async () => {
      try {
        const response = await axios.get(`${BACKENDURL}/user-info`, {
          withCredentials: true,
        });
        if (response.status === 200) {
          setUserData(response.data);
        }
      } catch (error) {
        if (error.response && error.response.status === 401) {
          console.error("User is not authenticated");
        } else {
          console.error("Error fetching user data:", error);
        }
        setUserData(null);
      } finally {
        setLoading(false);
      }
    };

    getUser();
  }, []);

  return (
    <AuthContext.Provider value={{ userData, setUserData, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};
