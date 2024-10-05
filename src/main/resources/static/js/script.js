console.log("script loaded");

let currentTheme = getTheme();

const changeThemeButton = document.querySelector("#theme_change_button");

document.querySelector("html").classList.add(currentTheme);
changeText();

changeThemeButton.addEventListener("click", (event) => {
    console.log("change theme btn clicked");
    changeTheme();
    changeText();
});

function changeTheme(){
    let isDark = document.querySelector("html").classList.toggle("dark");
    if(isDark) {
        setTheme("dark");
    } else {
        setTheme("light");
    }
}

function changeText(){
    let isDark = document.querySelector("html").classList.contains("dark");
    if(isDark) {
        changeThemeButton.querySelector("span").textContent = "LIT";
    } else {
        changeThemeButton.querySelector("span").textContent = "DRK";
    }
}


function setTheme(theme){
    localStorage.setItem("theme", theme);
}

function getTheme(){
    let storedTheme = localStorage.getItem("theme");
    if(storedTheme === null) {
        if(document.querySelector("html").classList.contains("dark")) {
            storedTheme = "dark";
        } else {
            storedTheme = "light";
        }
    }
    return storedTheme;
}

function logout() {
    Swal.fire({
        title: "Logout !!!",
        text: "Are You Sure...?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Confirm"
      }).then((result) => {
        if (result.isConfirmed) {
            const url = `http://localhost:8081/logout`;
            window.location.replace(url);
        }
      });
}