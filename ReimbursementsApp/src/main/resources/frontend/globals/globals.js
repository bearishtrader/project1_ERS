let domain = "http://localhost:9000";
const currency = "$";
const reimbType = ['LODGING', 'TRAVEL', 'FOOD', 'OTHER'];
const reimbStatus = ['Pending','Approved','Denied'];
async function logout(){
    await fetch("http://localhost:9000/api/logout", {
        method: "DELETE"
    })
    window.location.href = "../"
}