import React, { useEffect, useState } from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "../components/ui/tabs";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "../components/ui/card";
import { Participants } from "../types/Participants";
import axios from "axios";
import { toast } from "react-toastify";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { Assignment } from "../types/Assignment";
import CreateAssignment from "./CreateAssignment";
import { Button } from "../components/ui/button";
import { Calendar, Users, Bell, Plus, X, Clock, Download, MessageCircle } from "lucide-react";
import ParticipantsTab from "../components/ParticipantsTab";


const ViewClass: React.FC = () => {
  const { classroomId } = useParams<{ classroomId: string }>();
  const [participants, setParticipants] = useState<Participants[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [announcements, setAnnouncements] = useState<any[]>([]);
  const [showAnnouncementModal, setShowAnnouncementModal] = useState(false);
  const userEmail = localStorage.getItem("useremail");
  const navigate = useNavigate();
  const location = useLocation();
  const role = location.state?.role;
  console.log("Role : ", role);
  // const { user } = useSelector((state: any) => state.user) || {};
  // console.log("user : ", user);

  useEffect(() => {
    const fetchAssignments = async () => {
      try {
        const { data } = await axios.get(
          `http://localhost:8080/api/classrooms/assignments/${classroomId}/assignments`,
          { withCredentials: true }
        );
        console.log("assignments ", data);
        setAssignments(data);
      } catch (error) {
        console.error(error);
      }
    };

    fetchAssignments();
  }, [classroomId]);

  // Fetch Announcements
  useEffect(() => {
    const fetchAnnouncements = async () => {
      try {
        const { data } = await axios.get(
          `http://localhost:8080/api/announcements/${classroomId}`,
          { withCredentials: true }
        );
        setAnnouncements(data);
      } catch (error) {
        console.error(error);
      }
    };

    fetchAnnouncements();
  }, [classroomId]);

  // Function to handle adding a new announcement
  const handleAddAnnouncement = async (title: string, content: string) => {
    try {
      await axios.post(
        `http://localhost:8080/api/announcements/create`,
        {
          title,
          content,
          classId: classroomId,
          userEmail,
        },
        { withCredentials: true }
      );

      toast.success("Announcement added successfully!");
      setShowAnnouncementModal(false);

      // Fetch updated announcements
      const { data } = await axios.get(
        `http://localhost:8080/api/announcements/${classroomId}`,
        { withCredentials: true }
      );
      setAnnouncements(data);
    } catch (error) {
      toast.error("Failed to add announcement.");
      console.error(error);
    }
  };

  // Fetch participants
  useEffect(() => {
    const fetchParticipants = async () => {
      try {
        const { data } = await axios.get(
          `http://localhost:8080/api/classrooms/${classroomId}/participants`,
          {
            withCredentials: true,
          }
        );
        setParticipants(data);
        console.log(" participants : ", data);
        if (data == null) {
          toast.error("No participants found");
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : "An error occurred");
      } finally {
        setLoading(false);
      }
    };

    fetchParticipants();
  }, [classroomId]);


  const handleRoleUpdate = (userId: number, newRole: string) => {
    setParticipants((prevParticipants) =>
      prevParticipants.map((user) =>
        user.id === userId ? { ...user, role: newRole } : user
      )
    );
  };


  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="w-16 h-16 border-4 border-blue-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="p-6 bg-red-50 rounded-lg text-red-600">Error: {error}</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4">

      {/* <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-6"> */}
      <Tabs defaultValue="assignments" className="w-full max-w-7xl mx-auto">
        <TabsList className="bg-white p-1 rounded-xl shadow-sm mb-6">
          <TabsTrigger value="assignments" className="flex items-center space-x-2">
            <Calendar className="w-4 h-4" />
            <span>Assignments</span>
          </TabsTrigger>
          <TabsTrigger value="participants" className="flex items-center space-x-2">
            <Users className="w-4 h-4" />
            <span>Participants</span>
          </TabsTrigger>
          <TabsTrigger value="announcements" className="flex items-center space-x-2">
            <Bell className="w-4 h-4" />
            <span>Announcements</span>
          </TabsTrigger>
        </TabsList>

        <TabsContent value="assignments">
          <div className="flex justify-end mb-6">
            {role === "STUDENT" ?
              <>
              </> :

              <Button
                onClick={() => setShowModal(true)}
                className="bg-blue-600 hover:bg-blue-700 text-white flex items-center space-x-2"
              >
                <Plus className="w-4 h-4" />
                <span>New Assignment</span>
              </Button>
            }

          </div>

          <div className="">
            {assignments.length === 0 ? (
              <div className="col-span-full flex items-center justify-center p-12 bg-white rounded-xl shadow-sm">
                <p className="text-gray-500">No assignments available</p>
              </div>
            ) : (
              assignments.map((assignment) => (
                <Card
                  key={assignment.id}
                  className="m-2 w-full hover:shadow-lg transition-shadow duration-200 cursor-pointer bg-white"
                  onClick={() => navigate(`/classrooms/${classroomId}/assignments/${assignment.id}`)}
                >
                  <CardHeader>
                    <CardTitle className="text-xl font-semibold text-gray-800">
                      {assignment.title}
                    </CardTitle>
                    <CardDescription className="line-clamp-2">
                      {assignment.content}
                    </CardDescription>
                  </CardHeader>
                  {/* <CardContent className="space-y-4">
                      <div className="flex items-center space-x-2 text-sm text-gray-600">
                        <Clock className="w-4 h-4" />
                        <span>Due: {new Date(assignment.dueDate).toLocaleDateString()}</span>
                      </div>
                      {assignment.filePath && (
                        <a
                          href={assignment.filePath}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="inline-flex items-center space-x-2 text-blue-600 hover:text-blue-700"
                          onClick={(e) => e.stopPropagation()}
                        >
                          <Download className="w-4 h-4" />
                          <span>Download Materials</span>
                        </a>
                      )}
                    </CardContent> */}
                </Card>
              ))
            )}
          </div>
        </TabsContent>

        {/* <TabsContent value="participants">
          <Card className="bg-white shadow-sm">
            <CardHeader>
              <CardTitle className="text-2xl font-bold text-gray-800">Class Participants</CardTitle>
              <CardDescription>Total participants: {participants.length}</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                {participants.map((user) => (
                  <div
                    key={user.id}
                    className="p-4 rounded-lg bg-gradient-to-br from-gray-50 to-gray-100 border border-gray-200"
                  >
                    <div className="flex items-center space-x-4">
                      <div className="w-12 h-12 rounded-full bg-blue-600 flex items-center justify-center text-white font-bold">
                        {user.fullName.charAt(0)}
                      </div>
                      <div>
                        <h3 className="font-semibold text-gray-800">{user.fullName}</h3>
                        <p className="text-sm text-gray-600">{user.email}</p>
                        <span className="inline-block px-2 py-1 mt-2 text-xs font-medium rounded-full bg-blue-100 text-blue-800">
                          {user.role}
                        </span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent> */}
        <ParticipantsTab
          participants={participants || ""}
          classRoomId={classroomId || ""}
          onRoleUpdate={handleRoleUpdate}
        />

        <TabsContent value="announcements">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Class Announcements</h2>
            {role === "STUDENT" ?
              <>
              </> :
              <Button
                onClick={() => setShowAnnouncementModal(true)}
                className="bg-blue-600 hover:bg-blue-700 text-white flex items-center space-x-2"
              >
                <Plus className="w-4 h-4" />
                <span>New Announcement</span>
              </Button>
            }
          </div>

          <div className="grid gap-6 grid-cols-4">
            <div className="col-span-1">
              <Card className="bg-white shadow-sm sticky top-6">
                <CardHeader>
                  <CardTitle className="text-lg font-semibold">Upcoming Due</CardTitle>
                </CardHeader>
                <CardContent>
                  {assignments.length == 0 ?
                    <>
                      <h2>Currently no assignment available.</h2>
                    </>
                    :
                    assignments
                      .filter((assignment) => new Date(assignment.dueDate) > new Date())
                      .sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime())
                      .slice(0, 3)
                      .map((assignment) => (
                        <div
                          key={assignment.id}
                          className="p-3 rounded-lg bg-gray-50 mb-3 last:mb-0 border border-gray-100"
                        >
                          <h4 className="font-medium text-gray-800 mb-1">{assignment.title}</h4>
                          <p className="text-sm text-gray-600">
                            Due: {new Date(assignment.dueDate).toLocaleDateString()}
                          </p>
                        </div>
                      ))}
                </CardContent>
              </Card>
            </div>

            <div className="col-span-3 space-y-6">
              {announcements.map((announcement) => (
                <Card key={announcement.id} className="bg-white shadow-sm">
                  <CardHeader>
                    <div className="flex justify-between items-start">
                      <div>
                        <CardTitle className="text-xl font-semibold text-gray-800">
                          {announcement.title}
                        </CardTitle>
                        <CardDescription>
                          Posted by {announcement.author.email} on{" "}
                          {new Date(announcement.createdAt).toLocaleDateString()}
                        </CardDescription>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <p className="text-gray-700">{announcement.content}</p>

                    <div className="border-t pt-4">
                      <div className="flex items-center space-x-2">
                        <MessageCircle className="w-4 h-4 text-gray-400" />
                        <textarea
                          placeholder="Add a comment..."
                          className="flex-1 p-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200 outline-none resize-none"
                          rows={1}
                        />
                        <Button className="bg-blue-600 hover:bg-blue-700 text-white">
                          Comment
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </div>
        </TabsContent>
      </Tabs>

      {/* Modals */}
      {showModal && (
        // <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        //   <div className="bg-white rounded-xl p-6 w-full max-w-2xl">
        //     <div className="flex justify-between items-center mb-4">
        //       <h2 className="text-2xl font-bold">Create Assignment</h2>
        //       <button
        //         onClick={() => setShowModal(false)}
        //         className="p-2 hover:bg-gray-100 rounded-full transition-colors"
        //       >
        //         <X className="w-5 h-5" />
        //       </button>
        //     </div>
        <CreateAssignment onClose={() => setShowModal(false)} />
        //   </div>
        // </div>
      )}

      {showAnnouncementModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 w-full max-w-2xl">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-2xl font-bold">New Announcement</h2>
              <button
                onClick={() => setShowAnnouncementModal(false)}
                className="p-2 hover:bg-gray-100 rounded-full transition-colors"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Title
                </label>
                <input
                  type="text"
                  id="announcementTitle"
                  className="w-full p-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200 outline-none"
                  placeholder="Enter announcement title"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Content
                </label>
                <textarea
                  id="announcementContent"
                  rows={4}
                  className="w-full p-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200 outline-none resize-none"
                  placeholder="Enter announcement content"
                />
              </div>

              <div className="flex justify-end">
                <Button
                  onClick={() => {
                    const title = (document.getElementById("announcementTitle") as HTMLInputElement).value.trim();
                    const content = (document.getElementById("announcementContent") as HTMLTextAreaElement).value.trim();
                    if (!title || !content) {
                      toast.error("Please fill in both fields");
                      return;
                    }
                    handleAddAnnouncement(title, content);
                  }}
                  className="bg-blue-600 hover:bg-blue-700 text-white"
                >
                  Post Announcement
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ViewClass;