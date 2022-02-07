let domain = "http://34.221.46.159:9000";
//let domain = "http://localhost:9000";
const currency = "$";
const reimbType = ['LODGING', 'TRAVEL', 'FOOD', 'OTHER'];
const reimbStatus = ['Pending','Approved','Denied'];
async function logout(){
    await fetch(`${domain}/api/logout`, {
        method: "DELETE"
    })
    window.location.href = "../"
}
