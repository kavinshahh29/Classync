import React from "react";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import Home from "./pages/Home";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import Nav from "./components/nav/Nav";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import MyClasses from "./pages/MyClasses";
import ViewClass from "./pages/ViewClass";
import ViewAssignment from "./pages/ViewAssignment";
import CalendarPage from "./pages/CalendarPage";
import UpdateAssignment from "./pages/UpdateAssignment";
import UserGuide from "./pages/user-guide/user-guide-page";
import AdminRoute from "./utils/AdminRoute";
import AdminClassesPage from "./pages/AdminClassesPage";
import AllUsersPage from "./pages/AllUserPage";
import ClassroomParticipants from "./pages/ClassroomParticipant";

const App: React.FC = () => {
  const dispatch = useDispatch();
  const user = useSelector((state: any) => state.user.user);

  const loadUser = async () => {
    try {
      const { data } = await axios.get("http://localhost:8080/api/user", {
        withCredentials: true,
        headers: {
          Accept: "application/json",
        },
      });

      if (data) {
        localStorage.setItem("useremail", data.email);
        dispatch({ type: "SET_USER", payload: data });
      }
    } catch (error) {
      console.error(error);
      dispatch({ type: "SET_USER", payload: null });
    }
  };

  React.useEffect(() => {
    loadUser();
  }, []);

  // If still loading
  if (user === undefined) {
    return (
      <div className="flex items-center justify-center h-screen text-white text-2xl">
        Loading...
      </div>
    );
  }

  return (
    <div className="flex flex-col min-h-screen bg-gradient-to-b from-gray-900 to-gray-900 font-poppins">
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />
      <Router>
        <Nav />
        <div className="flex-grow">
          <Routes>
            {/* Home Page - Public */}
            <Route path="/" element={<Home />} />

            {/* Admin Routes - only admin user */}
            <Route element={<AdminRoute />}>
              <Route path="/admin/all-classes" element={<AdminClassesPage />} />
              <Route path="/admin/all-users" element={<AllUsersPage />} />
              <Route path="/classroom/:classroomId" element={<ClassroomParticipants />} />
            </Route>

            {/* Protected Routes - only logged in users */}
            {user ? (
              <>
                <Route path="/user-guide" element={<UserGuide />} />
                <Route path="/myclasses" element={<MyClasses />} />
                <Route path="/classrooms/:classroomId" element={<ViewClass />} />
                <Route path="/classrooms/:classroomId/assignments/:assignmentId" element={<ViewAssignment />} />
                <Route path="/classrooms/:classroomId/assignments/:assignmentId/edit" element={<UpdateAssignment onClose={() => { }} onAssignmentUpdated={() => { }} />} />
                <Route path="/calendar" element={<CalendarPage />} />
              </>
            ) : (
              // if not logged in, trying to access these -> redirect to home
              <>
                <Route path="/user-guide" element={<Navigate to="/" replace />} />
                <Route path="/myclasses" element={<Navigate to="/" replace />} />
                <Route path="/classrooms/:classroomId" element={<Navigate to="/" replace />} />
                <Route path="/classrooms/:classroomId/assignments/:assignmentId" element={<Navigate to="/" replace />} />
                <Route path="/classrooms/:classroomId/assignments/:assignmentId/edit" element={<Navigate to="/" replace />} />
                <Route path="/calendar" element={<Navigate to="/" replace />} />
              </>
            )}
          </Routes>
        </div>
      </Router>
    </div>
  );
};

export default App;
