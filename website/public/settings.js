document.addEventListener("DOMContentLoaded", () => {
    const beerTypeInput = document.getElementById("beerType");
    const speedInput = document.getElementById("speed");
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

    // Update speed input placeholder and range dynamically based on beer type
    beerTypeInput.addEventListener("change", () => {
        const beerType = beerTypeInput.value;
        if (beerType && validRanges[beerType]) {
            const range = validRanges[beerType];
            speedInput.placeholder = `Enter Speed (${range.min} - ${range.max})`;
            speedInput.min = range.min;
            speedInput.max = range.max;
        } else {
            speedInput.placeholder = "Enter Speed";
            speedInput.removeAttribute("min");
            speedInput.removeAttribute("max");
        }
    });

    // Validate inputs
    function validateInputs() {
        const beerType = beerTypeInput.value;
        const speed = parseInt(speedInput.value, 10);

        if (!beerType || isNaN(speed)) {
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
        }
    }

    validateButton.addEventListener("click", validateInputs);
});
