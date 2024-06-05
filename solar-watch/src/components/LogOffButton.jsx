import React from 'react'
import { useNavigate } from 'react-router-dom';

function LogOffButton() {
    const navigate = useNavigate()
    function handleLogOffButton() {
        localStorage.removeItem("jwt");
        navigate("/login");
    }
    return (
        <div><button onClick={handleLogOffButton}>Log Out</button></div>
    )
}

export default LogOffButton