import { Navigate, Outlet } from "react-router-dom";
import { useSelector } from "react-redux";

const AdminRoute = () => {
  const user = useSelector((state: any) => state.user.user);

  if (user === undefined || user === null) {
    return <div className="text-white text-center mt-10">Loading...</div>;
  }

  const isAdmin = user.email === import.meta.env.VITE_ADMIN_EMAIL;

  return isAdmin ? <Outlet /> : <Navigate to="/" replace />;
};

export default AdminRoute;
