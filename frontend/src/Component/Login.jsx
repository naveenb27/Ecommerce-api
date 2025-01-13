import { useNavigate } from "react-router-dom";
import { useAuth } from "../Context/AuthContext"
import google from "../assets/Images/Google.png"
import github from "../assets/Images/github.jpeg"

const Login = () => {
    

    const {userData} = useAuth();
    const navigate = useNavigate();
    try{
        if(userData.name){
            console.log(userData);
            navigate("/dashboard");
        }    
    }catch(e){
        console.log(userData);
        console.log(e);
    }
    
    const googleLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/google"
    }

    const githubLogin = () => {
       window.location.href = "http://localhost:8080/oauth2/authorization/github"
    }

    return (
        <div className="flex justify-center items-center min-h-screen">
            <div className="flex flex-col border border-solid gap-6 p-6 bg-white rounded-lg shadow-lg w-[400px]">
                <h2 className="text-[26px] text-center font-semibold text-gray-800">LOGIN</h2>
                <div className="btns flex gap-4 justify-center">
                    <button 
                        onClick={googleLogin} 
                        className="border border-solid border-gray-300 px-4 py-4 rounded-full w-[80px] h-[80px] flex items-center justify-center transition-transform transform hover:scale-105 hover:bg-blue-100">
                        <img className="rounded-full object-cover w-full h-full" src={google} alt="google"/>
                    </button>
                    <button 
                        onClick={githubLogin} 
                        className="border border-solid border-gray-300 px-4 py-4 rounded-full w-[80px] h-[80px] flex items-center justify-center transition-transform transform hover:scale-105 hover:bg-gray-100">
                        <img className="rounded-full object-cover w-full h-full" src={github} alt="github"/>
                    </button>
                </div>
            </div>
        </div>
    )
}

export default Login;
