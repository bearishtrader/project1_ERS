window.onload = async () => {
  let response = await fetch(`${domain}/api/check-session`);
  let result = await response.json();

  // Go to login if no session is found
  if(!result.successful)
      window.location.href = "../";

  // Go to Financial Manager dashboard if role is "FinManager"
  if(result.data.role === "FinManager")
      window.location.href = "../finmanager-dashboard"

  // Add role, username and user-id
  let roleElem = document.createElement("h2");
  roleElem.innerText = result.data.role;
  roleElem.setAttribute("class", "text-right-inline");

  let usernameElem = document.createElement("h3");
  usernameElem.innerText = result.data.username;
  usernameElem.setAttribute("class", "text-right-inline");

  let userIdElem = document.createElement("h4");
  userIdElem.innerText = result.data.userId;
  userIdElem.setAttribute("class", "text-right-inline");
  userIdElem.setAttribute("id", "user-id");

  let userInfo = document.getElementById("user-info");
  userInfo.appendChild(roleElem);
  userInfo.appendChild(usernameElem);
  userInfo.appendChild(userIdElem);

  populateReimbursements();
}

async function createReimbursement(e){
  e.preventDefault();
    
  let reimbDescriptionElem = document.getElementById("reimb-description");
  let reimbDescription = reimbDescriptionElem.value;
  let reimbAmountElem = document.getElementById("reimb-amount");
  let reimbAmount = reimbAmountElem.value;
  let reimbAuthorElem = document.getElementById("user-id");
  let reimbAuthor = Number(reimbAuthorElem.innerText);
  let reimbTypeIdElem = document.getElementById("reimb-type-id");
  let reimbTypeId = reimbTypeIdElem.value;
  let reimbSubmitted = new Date().toJSON();

  // Validate the fields
  let createErrorTextElem = document.getElementById("create-error-text");
  if (reimbDescription=="") {
    createErrorTextElem.setAttribute("class", "text red text-smaller");
    createErrorTextElem.innerText = "Description can not be blank.";
    return;
  }
  if (reimbAmount==null || reimbAmount<=0.0) {
    createErrorTextElem.setAttribute("class", "text red text-smaller");
    createErrorTextElem.innerText = "Amount must be greater than $0.00";
    return;
  }
  if (reimbTypeId == "Choose One") {
    createErrorTextElem.setAttribute("class", "text red text-smaller");
    createErrorTextElem.innerText = "Choose one expense reimbursement type from dropdown list.";
    return;
  }
  
  let response = await fetch(`${domain}/api/reimbursements`,{
      method: "POST",
      body: JSON.stringify({          
          reimbAmount: reimbAmount,
          reimbDescription: reimbDescription,
          reimbSubmitted: reimbSubmitted,
          reimbAuthor: reimbAuthor,
          reimbStatusId: 1, // 1 means Pending
          reimbTypeId: reimbTypeId
      })
  })

  let result = await response.json()

  if (result.successful) {
    reimbAmountElem.value = 0.0;
    reimbDescriptionElem.value = "";
    reimbTypeId.value = 0;  // default to Choose One
    createErrorTextElem.setAttribute("class", "text green text-smaller");
    createErrorTextElem.innerText = result.message;
    populateReimbursements();
  } else {
    createErrorTextElem.setAttribute("class", "text red text-smaller");
    createErrorTextElem.innerText = result.message;
  }
}

async function deleteReimb(e){

  let createErrorTextElem = document.getElementById("create-error-text");
  
  let reimbId = e.target.id.slice("delete-btn-".length,e.target.id.length)
  
  let response = await fetch(`${domain}/api/reimbursements/${reimbId}`,{
      method: "DELETE"
  });
  let result = await response.json();
  if (result.successful) {
    createErrorTextElem.setAttribute("class", "text green");
    createErrorTextElem.innerText = result.message;
    populateReimbursements();
  } else {
    createErrorTextElem.setAttribute("class", "text red text-smaller");
    createErrorTextElem.innerText = result.message;
  }
}

async function populateReimbursements(){
  // The behavior of our "/api/reimbursements" endpoint varies according to the user's role, which is retrieved from our session
  // For the role=="Employee" case, we are fetching only this employee's reimbursement submissions
  let response = await fetch(`${domain}/api/reimbursements`, {method: "GET"});
  let result = await response.json();
  if (!result.successful) {
    let createErrorTextElem = document.getElementById("create-error-text");
    createErrorTextElem.setAttribute("class", "text red text-smaller");
    createErrorTextElem.innerText = result.message;
    if (result.message=="No reimbursements found. (4)") {
      // Clear container and place our header
      let reimbContainer = document.getElementById("reimb-container");  
      reimbContainer.innerHTML = `
      <tr class="reimb-item">
        <td><b><u>Item#</u></b></td>
        <td><b><u>Date Submitted</u></b></td>
        <td><b><u>Date Resolved</u></b></td>
        <td><b><u>Description</u></b></td>
        <td><b><u>Amount</u></b></td>
        <td><b><u>Resolver</u></b></td>
        <td><b><u>Status</u></b></td>
        <td><b><u>Type</u></b></td>
        <td><b><u>Action</u></b></td>
      </tr>
      `;
    }
    return;
  }

  let data = result.data;  
  data.sort((a,b) => {
      if(a.reimbId > b.reimbId)
          return -1;
      else return 1;
  })

  // Clear container and place our header
  let reimbContainer = document.getElementById("reimb-container");  
  reimbContainer.innerHTML = `
  <tr class="reimb-item">
    <td><b><u>Item#</u></b></td>
    <td><b><u>Date Submitted</u></b></td>
    <td><b><u>Date Resolved</u></b></td>
    <td><b><u>Description</u></b></td>
    <td><b><u>Amount</u></b></td>
    <td><b><u>Resolver</u></b></td>
    <td><b><u>Status</u></b></td>
    <td><b><u>Type</u></b></td>
    <td><b><u>Action</u></b></td>
  </tr>
  `;
  
  // Create each line item which is one of our fetch ersReimbursement records for this employee and append it to our reimbContainer  
  for (let ersReimbursement of data) {  
      let reimbItemElem = document.createElement("tr");
      reimbItemElem.className = "reimb-item";
      reimbItemElem.reimbId = ersReimbursement.reimbId;
      let dateSubmitted = new Date(ersReimbursement.reimbSubmitted);
      let dateResolved = "*** Unresolved ***";
      let dateResolvedClass = "reimb-resolved green";
      if (ersReimbursement.reimbResolved != null) dateResolved = new Date(ersReimbursement.reimbResolved);
      else dateResolvedClass="reimb-resolved red";
      let resolverName = "N/A";
      if (ersReimbursement.reimbResolver==null) resolverName="N/A"; // resolver id who is a FinManager ersUser is not filled in
      else {
        resolverName = ((ersReimbursement.resolverFirstname ==null) ? "":ersReimbursement.resolverFirstname) + " " + 
          (ersReimbursement.resolverLastname==null?"":ersReimbursement.resolverLastname) + " (" + ersReimbursement.resolverEmail+ ")";        
      }
      let reimbStatusCode = reimbStatus[ersReimbursement.reimbStatusId - 1];
      let reimbStatusCodeClass = "reimb-status";
      if (reimbStatusCode=='Approved') {
        reimbStatusCodeClass = "reimb-status green";
        dateResolvedClass="reimb-resolved green"
      }
      else if (reimbStatusCode=='Denied') {
        reimbStatusCodeClass = "reimb-status red";
        dateResolvedClass="reimb-resolved red"
      }
      reimbItemElem.innerHTML = 
      ` <td class="reimb-id">${reimbItemElem.reimbId}</td> 
        <td class="reimb-submitted">${dateSubmitted}</td>
        <td class="${dateResolvedClass}">${dateResolved}</td>
        <td class="reimb-description">${ersReimbursement.reimbDescription}</td>
        <td class="reimb-amount"><span>${currency}</span>${ersReimbursement.reimbAmount}</td>
        <td class="reimb-resolver">${resolverName}</td>
        <td class="${reimbStatusCodeClass}">${reimbStatusCode}</td>
        <td class="reimb-type">${reimbType[ersReimbursement.reimbTypeId - 1]}</td>
        <td class="reimb-delete-btns">${ersReimbursement.reimbStatusId == 1 ? `<button class="btn btn-danger" id="delete-btn-${ersReimbursement.reimbId}" onclick="deleteReimb(event)">Delete</button>`: ''}</td>
      `
      
      reimbContainer.appendChild(reimbItemElem);      
  } 
}