import { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginForm() {
    const navigate = useNavigate();

    const [solarUsername, setSolarUsername] = useState("");
    const [password, setPassword] = useState("");
    const [showMessage, setShowMessage] = useState(false);

    function handleSolarUsernameChange(e) {
        setSolarUsername(e.target.value);
    }

    function handlePasswordChange(e) {
        setPassword(e.target.value);
    }

    async function handleLogin() {
        const response = await fetch('/solar-user/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                solarUsername: solarUsername,
                password: password,
            }),
        });
        if (!response.ok) {
            setShowMessage(true);
            throw new Error("Unknown solaruser: " + JSON.stringify(response.body));
        } else {
            const data = await response.json();
            localStorage.setItem("jwt", data.jwt);
            navigate(`/solar-watch`);
        }
    }

    return (
        <div className="container">
            <h1>Solar:::Watch</h1>
            <form>
                <div>
                    {showMessage ? (
                        <p style={{ color: "red" }}>Incorrect password or user!</p>
                    ) : null}
                    <label htmlFor="loginName">
                        {" "}
                        Solar username:
                        <input
                            type="text"
                            name="loginName"
                            id="loginName"
                            value={solarUsername}
                            onChange={handleSolarUsernameChange}
                        />
                    </label>
                </div>
                <div>
                    <label htmlFor="loginPassword">
                        {" "}
                        Password:
                        <input
                            type="password"
                            name="loginPassword"
                            id="loginPassword"
                            value={password}
                            onChange={handlePasswordChange}
                        />
                    </label>
                </div>
            </form>
            <div>
                <button onClick={() => navigate("/registration")}>Sign up</button>
                <button onClick={() => handleLogin()}>Log in</button>
            </div>
        </div>
    );
}

export default LoginForm;
