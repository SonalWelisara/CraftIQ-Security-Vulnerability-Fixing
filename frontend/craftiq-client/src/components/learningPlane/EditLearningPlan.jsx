import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";
function EditLearningPlan() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [learningPlan, setLearningPlan] = useState(null);
  const [title, setTitle] = useState("");
  const [weeks, setWeeks] = useState([]);

// Fetch the learning plan
useEffect(() => {
   const token = JSON.parse(localStorage.getItem("user"))?.token;
  axios
    .get(`http://localhost:8086/api/learningPlans/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then((res) => {
      const data = res.data;

      const weekObjects = data.weeks.map((week, index) => ({
        week,
        milestones: data.milestones.slice(index * 3, index * 3 + 3) || [],
      }));

      setLearningPlan(data);
      setTitle(data.title);
      setWeeks(weekObjects);
    })
    .catch((err) => {
      console.error("Error fetching learning plan:", err);
    });
}, [id]);

  const addMilestone = (weekIndex) => {
    const updatedWeeks = [...weeks];
    if (updatedWeeks[weekIndex].milestones.length < 3) {
      updatedWeeks[weekIndex].milestones.push({
        title: "",
        date: "",
        completed: false,
      });
      setWeeks(updatedWeeks);
    }
  };

  const addWeek = () => {
    setWeeks([
      ...weeks,
      {
        week: `Week ${weeks.length + 1}`,
        milestones: [
          {
            title: "",
            date: "",
            completed: false,
          },
        ],
      },
    ]);
  };

  const handleMilestoneChange = (weekIndex, milestoneIndex, field, value) => {
    const updatedWeeks = [...weeks];
    updatedWeeks[weekIndex].milestones[milestoneIndex][field] =
      field === "completed" ? value === "true" : value;
    setWeeks(updatedWeeks);
  };

  const handleWeekChange = (weekIndex, value) => {
    const updatedWeeks = [...weeks];
    updatedWeeks[weekIndex].week = value;
    setWeeks(updatedWeeks);
  };

const handleSave = async () => {
  const token = JSON.parse(localStorage.getItem("user"))?.token;

  const updatedMilestones = weeks.flatMap((w) => w.milestones);
  const updatedWeekStrings = weeks.map((w) => w.week);

  const updatedPlan = {
    ...learningPlan,
    title,
    weeks: updatedWeekStrings,
    milestones: updatedMilestones,
  };

  try {
    await axios.put(`http://localhost:8086/api/learningPlans/${id}`, updatedPlan, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    Swal.fire({
      icon: "success",
      title: "Updated!",
      text: "Learning Plan updated successfully!",
      confirmButtonColor: "#3085d6",
    });

    navigate("/user/learning-plane");
  } catch (err) {
    console.error("Failed to update plan:", err);

    Swal.fire({
      icon: "error",
      title: "Update Failed",
      text: "Failed to update the learning plan.",
      confirmButtonColor: "#d33",
    });
  }
};

  if (!learningPlan) return <p>Loading...</p>;

  return (
    <div className="container mb-50 mt-50">
      <div className="w-95 w-md-75 w-lg-60 w-xl-55 mx-auto mb-6 text-center">
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="form-control mb-4 text-center"
          style={{ fontSize: "24px", fontWeight: "bold" }}
        />
      </div>

      <div className="row">
        <div className="col-md-12">
          <div className="schedule-table">
            <table className="table bg-white">
              <tbody>
                {weeks.map((weekObj, weekIndex) => (
                  <tr key={weekIndex}>
                    <td className="day" style={{ minWidth: "150px" }}>
                      <input
                        type="text"
                        value={weekObj.week}
                        onChange={(e) =>
                          handleWeekChange(weekIndex, e.target.value)
                        }
                        className="form-control mb-2"
                      />
                      <button
                        className="btn btn-sm btn-success"
                        onClick={() => addMilestone(weekIndex)}
                        disabled={weekObj.milestones.length >= 3}
                      >
                        + Milestone
                      </button>
                    </td>

                    {weekObj.milestones.map((milestone, i) => (
                      <td className="active" key={i}>
                        <input
                          type="text"
                          value={milestone.title}
                          onChange={(e) =>
                            handleMilestoneChange(
                              weekIndex,
                              i,
                              "title",
                              e.target.value
                            )
                          }
                          className="form-control mb-2"
                          placeholder="Milestone Title"
                        />
                        <input
                          type="text"
                          value={milestone.date}
                          onChange={(e) =>
                            handleMilestoneChange(
                              weekIndex,
                              i,
                              "date",
                              e.target.value
                            )
                          }
                          className="form-control mb-2"
                          placeholder="Date or Description"
                        />
                        <select
                          className="form-control"
                          value={milestone.completed}
                          onChange={(e) =>
                            handleMilestoneChange(
                              weekIndex,
                              i,
                              "completed",
                              e.target.value
                            )
                          }
                        >
                          <option value="false">Incomplete</option>
                          <option value="true">Completed</option>
                        </select>
                      </td>
                    ))}

                    {weekObj.milestones.length < 3 &&
                      [...Array(3 - weekObj.milestones.length)].map((_, i) => (
                        <td key={`empty-${i}`} />
                      ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="text-center mt-3">
            <button className="btn btn-primary" onClick={addWeek}>
              + Add Week
            </button>
          </div>
        </div>
      </div>

      <div className="text-center mt-4">
        <button className="btn btn-second" onClick={handleSave}>
          Save Learning Plan
        </button>
      </div>
    </div>
  );
}

export default EditLearningPlan;
