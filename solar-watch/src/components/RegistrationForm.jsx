import { useState } from 'react'
import { useNavigate } from 'react-router'

function RegistrationForm() {
    const navigate = useNavigate();
    const [solarUsername, setSolarUsername] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [showMessage, setShowMessage] = useState(false);

    async function postSolarUser(e) {
        e.preventDefault();
        try {
            const response = await fetch('/solar-user/register', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ solarUsername, password, firstName, lastName, email }),
            });

            if (!response.ok) {
                setShowMessage(true);
                throw new Error('Server error: ' + response.status);
            }
            navigate("/login");
        } catch (error) {
            console.error(error.message + " - Please choose another username.")
        }
    }

    function handleNameChange(e) {
        setSolarUsername(e.target.value)
    }

    function handlePasswordChange(e) {
        setPassword(e.target.value);
    }

    function handleFirstNameChange(e) {
        setFirstName(e.target.value);
    }

    function handleLastNameChange(e) {
        setLastName(e.target.value);
    }

    function handleEmailChange(e) {
        setEmail(e.target.value);
    }

    function togglePasswordVisibility() {
        setShowPassword(!showPassword);
    }

    return (
        <div className="container">
            <h1>Registration form: </h1>
            <form onSubmit={postSolarUser}>
                <div>
                    {showMessage ? <p style={{ color: 'orange' }}>Unfortunately, this Solar Username is already taken. Please choose another one!</p> : null}

                    <div><label htmlFor="SolarUsername"> Solar-Username:
                        <input type="text"
                            name="SolarUsername"
                            id="SolarUsername"
                            value={solarUsername}
                            onChange={handleNameChange}
                        />
                    </label>
                    </div>
                    <div>
                        <label htmlFor="RegisterPassword"> Password:
                            <input type={showPassword ? 'text' : 'password'}
                                name="RegisterPassword"
                                id="RegisterPassword"
                                value={password}
                                onChange={handlePasswordChange}
                            />
                            <div>
                            <button type="button" onClick={togglePasswordVisibility}>
                                {showPassword ? 'Hide' : 'Show'} Password
                            </button>
                            </div>
                        </label>
                    </div>
                    <div>
                        <label htmlFor="FirstName"> First name:
                            <input type="text"
                                name="FirstName"
                                id="FirstName"
                                value={firstName}
                                onChange={handleFirstNameChange}
                            />
                        </label>
                    </div>
                    <div>
                        <label htmlFor="LastName"> Last name:
                            <input type="text"
                                name="LastName"
                                id="LastName"
                                value={lastName}
                                onChange={handleLastNameChange}
                            />
                        </label>
                    </div>
                    <div>
                        <label htmlFor="Email"> Email:
                            <input type="text"
                                name="Email"
                                id="Email"
                                value={email}
                                onChange={handleEmailChange}
                            />
                        </label>
                    </div>
                </div>
                <button>Sign up</button>
            </form>
        </div>
    )
}

export default RegistrationForm