window.onload = async () => {
    let response = await fetch(`${domain}/api/check-session`);
    let result = await response.json();
  
    // Go to login if no session is found
    if(!result.successful)
        window.location.href = "../";
  
    // Go to employee dashboard if role is "Employee"
    if(result.data.role === "Employee")
        window.location.href = "../employee-dashboard"
  
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

    populateFinManagerReimbursements(0);
}

async function doStatusFilterChange(e){
    // If we are manually filtering the status clear any existing status or error message
    let createErrorTextElem = document.getElementById("create-error-text");    
    createErrorTextElem.innerText = "";
    populateFinManagerReimbursements(Number(e.target.value));
}

async function approveReimb(e){

    let createErrorTextElem = document.getElementById("create-error-text");
    // Get reimbId of ErsReimbursement to update the status of (Approve or Deny)
    let reimbId = e.target.id.slice("approve-btn-".length,e.target.id.length)
    let reimbResolved = new Date().toJSON();
    let reimbResolver = Number(document.getElementById("user-id").innerText);
    // Send PATCH request to approve (update) the expense reimbursement
    let response = await fetch(`${domain}/api/reimbursements/${reimbId}`,{
        method: "PATCH",
        body: JSON.stringify({          
            reimbId: reimbId,            
            reimbResolved: reimbResolved,
            reimbResolver: reimbResolver,
            reimbStatusId: 2, // 2 means Approved            
        })
    });
    let result = await response.json();
    if (result.successful) {
      createErrorTextElem.setAttribute("class", "text green");
      createErrorTextElem.innerText = result.message;
      populateFinManagerReimbursements(Number(document.querySelector('input[name = status-filter]:checked').value));
    } else {
      createErrorTextElem.setAttribute("class", "text red");
      createErrorTextElem.innerText = result.message;
    }
}

async function denyReimb(e) {
    let createErrorTextElem = document.getElementById("create-error-text");
    // Get reimbId of ErsReimbursement to update the status of (Approve or Deny)
    let reimbId = e.target.id.slice("deny-btn-".length,e.target.id.length)
    let reimbResolved = new Date().toJSON();
    let reimbResolver = Number(document.getElementById("user-id").innerText);
    // Send PATCH request to approve (update) the expense reimbursement
    let response = await fetch(`${domain}/api/reimbursements/${reimbId}`,{
        method: "PATCH",
        body: JSON.stringify({          
            reimbId: reimbId,            
            reimbResolved: reimbResolved,
            reimbResolver: reimbResolver,
            reimbStatusId: 3, // 3 means Denied
        })
    });
    let result = await response.json();
    if (result.successful) {
      createErrorTextElem.setAttribute("class", "text green");
      createErrorTextElem.innerText = result.message;
      populateFinManagerReimbursements(Number(document.querySelector('input[name = status-filter]:checked').value));
    } else {
      createErrorTextElem.setAttribute("class", "text red text-smaller");
      createErrorTextElem.innerText = result.message;
    }
}

async function populateFinManagerReimbursements(filterStatus){  // Filter status is value attribute of each radio button status-filter-*
    // The behavior of our "/api/reimbursements" endpoint varies according to the user's role, which is retrieved from our session
    // For the role=="FinManager" case, we are fetching ALL employees' expense reimbursements
    let response = await fetch(`${domain}/api/reimbursements`, {method: "GET"});
    let result = await response.json();
    
    if (!result.successful) {
        let createErrorTextElem = document.getElementById("create-error-text");
        createErrorTextElem.setAttribute("class", "text red text-smaller");
        createErrorTextElem.innerText = result.message;
        if (result.message == "No reimbursements found. (2)") {
            let reimbContainer = document.getElementById("reimb-container");  
            reimbContainer.innerHTML = `
            <tr class="reimb-item">
            <td><b><u>Item#</u></b></td>
            <td><b><u>Date Submitted</u></b></td>
            <td><b><u>Date Resolved</u></b></td>
            <td><b><u>Description</u></b></td>
            <td><b><u>Amount</u></b></td>
            <td><b><u>Author</u></b></td>
            <td><b><u>Resolver</u></b></td>
            <td><b><u>Status</u></b></td>
            <td><b><u>Type</u></b></td>
            <td><b><u>Approve</u></b></td>
            <td><b><u>Deny</u></b></td>
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
      <td><b><u>Author</u></b></td>
      <td><b><u>Resolver</u></b></td>
      <td><b><u>Status</u></b></td>
      <td><b><u>Type</u></b></td>
      <td><b><u>Approve</u></b></td>
      <td><b><u>Deny</u></b></td>
    </tr>
    `;
    
    // Create each line item which is one of our fetch ersReimbursement records for ALL employees and append it to our reimbContainer
    for (let ersReimbursement of data) {
        if (filterStatus>0 && filterStatus != ersReimbursement.reimbStatusId) continue; // Show All=0, Pending=1, Approved=2, Denied=3
        
        let reimbItemElem = document.createElement("tr");
        reimbItemElem.className = "reimb-item";
        reimbItemElem.reimbId = ersReimbursement.reimbId;
        let dateSubmitted = new Date(ersReimbursement.reimbSubmitted);
        let dateResolved = "*** Unresolved ***";
        let dateResolvedClass = "reimb-resolved green";
        if (ersReimbursement.reimbResolved != null) dateResolved = new Date(ersReimbursement.reimbResolved);
        else dateResolvedClass="reimb-resolved red";
        let authorName = "N/A"; // This should never happen as someone had to have created the original reimbursement request
        if (ersReimbursement.reimbAuthor==null) authorName="N/A";
        else {
            authorName = ((ersReimbursement.authorFirstname ==null) ? "":ersReimbursement.authorFirstname) + " " + 
            (ersReimbursement.authorLastname==null?"":ersReimbursement.authorLastname) + " (" + ersReimbursement.authorEmail+ ")";
        }
        let resolverName = "N/A";
        if (ersReimbursement.reimbResolver==null) resolverName="N/A"; // resolver id who is a FinManager ersUser is not filled in
        else {
          resolverName = ((ersReimbursement.resolverFirstname ==null) ? "":ersReimbursement.resolverFirstname) + " " + 
            (ersReimbursement.resolverLastname==null?"":ersReimbursement.resolverLastname) + " (" + ersReimbursement.resolverEmail+ ")";        
        }
        let reimbStatusCode = reimbStatus[ersReimbursement.reimbStatusId - 1];
        let reimbStatusCodeClass = "reimb-status";
        let approveBtnClass = "btn btn-primary";
        let denyBtnClass = "btn btn-danger";
        if (reimbStatusCode=='Approved') {
            reimbStatusCodeClass = "reimb-status green";
            approveBtnClass = "btn btn-primary disabled";
            dateResolvedClass="reimb-resolved green"
        }
        else if (reimbStatusCode=='Denied') {
            reimbStatusCodeClass = "reimb-status red";
            denyBtnClass = "btn btn-danger disabled";
            dateResolvedClass="reimb-resolved red"
        }
        reimbItemElem.innerHTML = 
        ` <td class="reimb-id">${reimbItemElem.reimbId}</td>
          <td class="reimb-submitted">${dateSubmitted}</td>
          <td class="${dateResolvedClass}">${dateResolved}</td>
          <td class="reimb-description">${ersReimbursement.reimbDescription}</td>
          <td class="reimb-amount"><span>${currency}</span>${ersReimbursement.reimbAmount}</td>
          <td class="reimb-author">${authorName}</td>
          <td class="reimb-resolver">${resolverName}</td>
          <td class="${reimbStatusCodeClass}">${reimbStatusCode}</td>
          <td class="reimb-type">${reimbType[ersReimbursement.reimbTypeId - 1]}</td>
          <td class="reimb-approve-btns"><button class="${approveBtnClass}" id="approve-btn-${ersReimbursement.reimbId}" onclick="approveReimb(event)">Approve</button></td>
          <td class="reimb-deny-btns"><button class="${denyBtnClass}" id="deny-btn-${ersReimbursement.reimbId}" onclick="denyReimb(event)">Deny</button></td>
        `
        
        reimbContainer.appendChild(reimbItemElem);      
    }
}