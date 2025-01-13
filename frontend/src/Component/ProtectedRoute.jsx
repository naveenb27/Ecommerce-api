import { Navigate } from "react-router-dom";
import { useAuth } from "../Context/AuthContext";

const ProtectedRoute = ({ children }) => {
  const { userData, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>; 
  }

  try{
    console.log(userData)
    if(!userData.name){
        console.log(userData);
        return <Navigate to="/" />;
    }    
  }catch(e){
      console.log(userData);
      console.log(e);
  }

  return children;
};

export default ProtectedRoute;
