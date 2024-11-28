<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Settings - Home Brew Hub</title>
    <link rel="stylesheet" href="{{ asset('style.css') }}">
</head>
<body>
    @include('partials.header')
    @include('partials.sidebar')

    <div class="content">
        <h2>Settings</h2>
        <p>Configure your brewing settings below.</p>

        <div class="settings-form">
            <label for="beerType">Beer Type:</label>
            <select id="beerType" name="beerType" required>
                <option value="">Select a Beer Type</option>
                <option value="Pilsner">Pilsner</option>
                <option value="Wheat">Wheat</option>
                <option value="IPA">IPA</option>
                <option value="Stout">Stout</option>
                <option value="Ale">Ale</option>
                <option value="AlcoholFree">Alcohol Free</option>
            </select>


            <label for="speed">Speed:</label>
            <input
                type="number"
                id="speed"
                name="speed"
                placeholder="Enter Speed"
                required
            >

            <label for="amount">Amount:</label>
            <input
                type="number"
                id="amount"
                name="amount"
                placeholder="Enter Amount"
                required
            >

            <button id="validateSettings" class="button">Validate</button>
        </div>

        <div id="validationMessage" class="message"></div>
    </div>

    <script>

        document.addEventListener("DOMContentLoaded", () => {
            const beerTypeInput = document.getElementById("beerType");
            const speedInput = document.getElementById("speed");
            const amountInput = document.getElementById("amount");
            const validateButton = document.getElementById("validateSettings");
            const validationMessage = document.getElementById("validationMessage");

            // Valid speed ranges for each beer type
            const validRanges = {
                Pilsner: { min: 0, max: 600 },
                Wheat: { min: 0, max: 300 },
                IPA: { min: 0, max: 150 },
                Stout: { min: 0, max: 200 },
                Ale: { min: 0, max: 100 },
                AlcoholFree: { min: 0, max: 125 },
            };

            validateButton.addEventListener("click", () => {
                const beerType = beerTypeInput.value;
                const speed = parseInt(speedInput.value, 10);
                const amount = parseInt(amountInput.value, 10);

                if (!beerType || isNaN(speed) || isNaN(amount)) {
                    validationMessage.textContent = "Please fill out all fields.";
                    validationMessage.className = "message error";
                    return;
                }

                const range = validRanges[beerType];
                if (speed < range.min || speed > range.max) {
                    validationMessage.textContent = `Error: Speed for ${beerType} must be between ${range.min} and ${range.max}.`;
                    validationMessage.className = "message error";
                } else {
                    validationMessage.textContent = "Settings are valid!";
                    validationMessage.className = "message success";
                    console.log(beerType,speed,amount);

                    // If validation is successful, send data to backend
                    sendDataToBackend({
                        beerType,
                        speed,
                        amount
                    });
                }
            });

            function sendDataToBackend(data) {
                fetch('http://localhost:8080/api/settings', {  // Change '/api/settings' to your actual endpoint
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => response.json())
                    .then(response => {
                        // Log raw response text for debugging
                        response.text().then(text => console.log("Raw response:", text));
                        return response.json();  // Continue processing JSON as normal
                    })
                    .then(data => {
                        console.log('Success:', data);
                        validationMessage.textContent = "Data WAS sent successfully!";
                        validationMessage.className = "message success";
                    })
                    .catch((error) => {
                        console.error('Error:', error);
                        validationMessage.textContent = "Error sending data.";
                        validationMessage.className = "message error";
                    });
            }
        });
    </script>
</body>
</html>
