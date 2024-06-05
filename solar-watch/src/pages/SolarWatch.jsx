import { useState } from 'react'
import LogOffButton from '../components/LogOffButton';
import '../pages/App.css'

const SolarWatch = () => {
    const [city, setCity] = useState("");
    const [date, setDate] = useState("");
    const [sunrise, setSunrise] = useState("");
    const [sunset, setSunset] = useState("");
    const [searchedCity, setSearchedCity] = useState("");
    const [searchedDate, setSearchedDate] = useState("");
    const [showMessage, setShowMessage] = useState(false);

    async function searchCity(e) {
        e.preventDefault();

        const token = localStorage.getItem('jwt');
        const requestOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        };

        const response = await fetch(`/solar-user/solar-watch?city=${city[0].toUpperCase() + city.substring(1)}&date=${date}`, requestOptions);
        const solarData = await response.json();
        setSunrise(solarData.sunrise);
        setSunset(solarData.sunset);
        setSearchedCity(city);
        setSearchedDate(date);
        setShowMessage(true);
    }

    function handleCityChange(e) {
        setCity(e.target.value);
    }

    function handleDateChange(e) {
        setDate(e.target.value);
    }

    return (
        <div className="container">
            <h1>SolarWatch</h1>
            <form onSubmit={searchCity}>
                <label htmlFor='CityToSearch'>City: 
                    <input
                        type="text"
                        id='CityToSearch'
                        name='CityToSearch'
                        value={city}
                        onChange={handleCityChange}
                    />
                </label>

                <label htmlFor='DateToSearch'>Date: 
                    <input
                        type="date"
                        id='DateToSearch'
                        name='DateToSearch'
                        value={date}
                        onChange={handleDateChange}
                    />
                </label>
                <button>Search</button>
            </form>
            <div>{showMessage ? <div><h1>The sun rises at {sunrise} </h1>
                <h1>and sets at {sunset}</h1>
                <h1>in {searchedCity[0].toUpperCase() + searchedCity.substring(1)} on {searchedDate}! </h1></div> : null}</div>
            <div><LogOffButton /></div>
        </div>
    )
}

export default SolarWatch