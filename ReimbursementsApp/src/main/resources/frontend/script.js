window.onload =async () => {
    let response = await fetch("http://localhost:9000/api/check-session");
    let result = await response.json();

    if(result.successful){
        window.location.href = `./${result.data.role.toLowerCase()}-dashboard`
    }
}

async function login(e){
    e.preventDefault();

    let emailInputElem = document.getElementById("email-input");
    let usernameInputElem = document.getElementById("username-input");
    let passwordInputElem = document.getElementById("password-input");    

    let response = await fetch("http://localhost:9000/api/login",{
        method: "POST",
        body: JSON.stringify({
            ersUsername: usernameInputElem.value,
            ersPassword: passwordInputElem.value,
            userEmail: emailInputElem.value
        })
    })

    let result = await response.json();
    console.log(result.successful);
    console.log(result.message);
    if(result.successful){ 
        document.getElementById("error-msg").innerText="";
        window.location.href = `./${result.data.role.toLowerCase()}-dashboard`
    }else{
        let errorMsgElem = document.getElementById("error-msg");
        errorMsgElem.innerText = result.message;
    }
}