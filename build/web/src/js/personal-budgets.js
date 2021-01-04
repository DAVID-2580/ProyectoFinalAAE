// UTILS
async function postData(url, data) {
  return fetchBody(url, "POST", data);
}

async function getData(url) {
  return fetchNoBody(url, "GET");
}

async function updateData(url, data) {
  return fetchBody(url, "PUT", data);
}

async function fetchNoBody(url, method) {
  const response = await fetch(url, {
    method: method,
    mode: "cors",
  });

  return response.json();
}

async function fetchBody(url, method, body) {
  // Opciones por defecto estan marcadas con un *
  const response = await fetch(url, {
    method: method, // *GET, POST, PUT, DELETE, etc.
    mode: "cors", // no-cors, *cors, same-origin
    body: JSON.stringify(body), // body data type must match "Content-Type" header
  });
  return response.json(); // parses JSON response into native JavaScript objects
}

let selectedChargeId = "";

function cancelDeleteCharge() {
  document.getElementById("closepopup").click();
}

async function confirmDeleteCharge() {
  Qual.info("Espere un momento...", "Estamos procesando su transacción");
  const deleteResult = await getData(
    "http://localhost/personal-budget/delete/charge/" + selectedChargeId
  );

  if (deleteResult.error) {
    Qual.errord("Ocurrio un error al eliminar", deleteResult.error.reason);
    return;
  }

  Qual.success("Cargo eliminado", "Se ha eliminado correctamente");

  setTimeout(() => location.reload(), 1500);
}

(function () {
  const BUDGET_LIMIT = 1;

  // BUTTONS
  const btn_create_budget = document.getElementById("create-budget");
  const btn_delete_budget = document.getElementById("delete-budget");
  const btn_update_budget = document.getElementById("update-budget");
  const btn_create_charge = document.getElementById("create-charge");

  const budget_list = document.getElementById("budget-list");
  const budget_counter = document.getElementById("budget-counter");
  const remaining_budgets = document.getElementById("remaining-budgets");
  const budgetNames = document.getElementById("budget-delete-names");
  const chargeBudgetNames = document.getElementById(
    "charge-budget-delete-names"
  );

  // OPERATIONS

  let budgetsMap = [];

  function createBudget() {
    let budgetCount = 0;

    for (let element in budgetsMap) budgetCount++;

    if (budgetCount >= BUDGET_LIMIT) {
      Qual.warning("Limite superado", "Ha superado el limite de pruesupuestos");
      return;
    }

    const budgetName = document.getElementById("budget-name").value;
    const budgetLimit = parseInt(document.getElementById("budget-limit").value);

    const budgetType = document.getElementById("monthly-budget").checked
      ? "MONTHLY"
      : "ANNUAL";
    const ownerId = getCookie("user_id");

    Qual.info("Espere porfavor ...", "Estamos procesando su solicitud");

    postData(`http://localhost/personal-budget/create`, {
      budget_name: budgetName,
      owner_id: ownerId,
      income: {
        amount: budgetLimit,
        type: budgetType,
      },
    }).then((response) => {
      if (response.error) {
        Qual.errord(
          "Ocurrio un error al registrar el presupuesto",
          response.error.reason
        );
        return;
      }

      Qual.success(
        "Registro Exitoso",
        `Se ha registrado el presupuesto: ${budgetName}`
      );

      setTimeout(() => location.reload(), 1500);
    });
  }

  async function updateBudget() {
    const budgetName = document.getElementById("budget-name").value;
    const budgetLimit = parseInt(document.getElementById("budget-limit").value);

    const budgetType = document.getElementById("monthly-budget").checked
      ? "MONTHLY"
      : "ANNUAL";

    const budgetId = document.getElementById("budget-delete-names").value;

    if (budgetId == "null") {
      Qual.info(
        "No hay selección",
        "Favor de seleccionar un presupuesto valido"
      );
      return;
    }

    Qual.info("Espere porfavor ...", "Estamos procesando su solicitud");

    const updaterResponse = await postData(
      `http://localhost/personal-budget/update/` + budgetId,
      {
        budget_name: budgetName,
        income: {
          amount: budgetLimit,
          type: budgetType,
        },
      }
    );

    if (updaterResponse.error) {
      Qual.errord("Error al actualizar", updaterResponse.error.reason);
      return;
    }

    Qual.success(
      "Actualización Correcta",
      "Su presupuesto ha sido actualizado"
    );

    setTimeout(() => {
      location.reload();
    }, 1000);
  }

  async function deleteBudget() {
    const budgetId = document.getElementById("budget-delete-names").value;

    if (budgetId == "null") {
      Qual.info(
        "No hay selección",
        "Favor de seleccionar un presupuesto valido"
      );
      return;
    }

    Qual.info(
      "Espere mientras un momento ...",
      "Intentando realizar su transacción"
    );

    const deleteResult = await getData(
      "http://localhost/personal-budget/delete/" + budgetId
    );

    if (deleteResult.error) {
      Qual.errord(
        "Ocurrio un error al intentar eliminar",
        deleteResult.error.reason
      );
      return;
    }

    Qual.success("Eliminación correcta", "Se ha eliminado el presupuesto");

    setTimeout(() => {
      location.reload();
    }, 1000);
  }

  async function findAll() {
    const ownerId = getCookie("user_id");

    const budgets = await getData(
      "http://localhost/personal-budget/get/all/" + ownerId
    );

    if (budgets.error) {
      budget_list.innerHTML =
        '<p class="m-2 text-bold">No hay presupuestos registrados</p>';
      return;
    }

    await budgets.success.budgets.forEach(async (budget) => {
      budgetsMap[budget.budget_name] = budget.budget_id;
      budgetNames.innerHTML += `
        <option value="${budget.budget_id}">${budget.budget_name}</option>
      `;

      chargeBudgetNames.innerHTML += `
      <option value="${budget.budget_id}">${budget.budget_name}</option>
    `;

      const htmlResult = await find(budget.budget_id);

      budget_list.innerHTML += htmlResult;
    });
  }

  async function find(budgetId) {
    const budget = await getData(
      "http://localhost/personal-budget/get/" + budgetId
    );

    let statusColor = "text-danger";

    if (budget.success.budget_status == "OK") statusColor = "text-success";
    if (budget.success.budget_status == "ALMOST_EXCEEDED")
      statusColor = "text-warning";

    const responseHtml = `
    <li class="nav-item">
    <a  class="nav-link">
        <i class="nav-icon ion ion-stats-bars text-success"></i>
        <p class="text-bold">
          ${budget.success.budget_name}
            <i class="fas fa-angle-left right"></i>
            <span class="right badge badge-dark">Presupuesto</span>
        </p>
    </a>
    <ul class="nav nav-treeview" style="margin-left: 20px">
        <li class="nav-item">
            <a  class="nav-link ${statusColor} text-bold">
                <i class="far fa-bars text-dark nav-icon"></i>
                Status: <p>${budget.success.budget_status}</p>
            </a>
        </li>

        <li class="nav-item">
            <a  class="nav-link">
                <i class="far fa-bars text-dark nav-icon"></i>
                <span class="text-bold">Tipo de ingreso: </span><p>${
                  budget.success.income.type == "MONTHLY" ? "MENSUAL" : "ANUAL"
                }</p>
            </a>
        </li>

        <li class="nav-item">
            <a  class="nav-link">
                <i class="far fa-bars text-dark nav-icon"></i>
                <span class="text-bold">Total Gastos: </span>$ <p>${
                  budget.success.month_total
                }</p>
            </a>
        </li>

        <li class="nav-item">
            <a  class="nav-link">
                <i class="far fa-bars text-dark nav-icon"></i>
                <span class="text-bold">Limite Mensual: </span>$ <p>${
                  budget.success.month_max
                }</p>
            </a>
        </li>

        <ul class="nav nav-pills nav-sidebar flex-column" data-accordion="false">                                                                
            <a  class="nav-link">
                <i class="nav-icon ion ion-android-arrow-down text-danger"></i>
                <p class="text-bold">
                    Gastos
                    <i class="fas fa-angle-left right"></i>
                    <span class="right badge badge-danger">Gastos</span>
                </p>                                                                    
            </a>`;

    let charges = "";

    budget.success.charges.forEach((charge) => {
      charges += `
      <li class="nav-item" style="margin-left: 20px">      
      <a  class="nav-link">
          <i class="far fa-bars text-dark nav-icon"></i>
          <p><span class="text-bold">Titulo: </span>${charge.title}</p>
          <button id="${charge.charge_id}" class="delete-charge btn btn-outline-danger" style="float: right; font-size: .7em">
            eliminar
          </button>
      </a>
      <a  class="nav-link">
          <i class="far fa-bars text-dark nav-icon"></i>
          <p><span class="text-bold">Fecha: </span>${charge.date}</p>
      </a>
      <a  class="nav-link">
          <i class="far fa-bars text-dark nav-icon"></i>
          <p><span class="text-bold">Hora: </span>${charge.time}</p>
      </a>
      <a  class="nav-link">
          <i class="far fa-bars text-dark nav-icon"></i>
          <p><span class="text-bold">Monto: </span>$${charge.amount}</p>
      </a>
      </li>
      `;
    });

    if (budget.success.charges.length === 0)
      charges = `
      <li class="nav-item" style="margin-left: 20px">
      <a  class="nav-link">
          <i class="far fa-bars text-dark nav-icon"></i>
          <p>No hay cargos</p>
      </a>
      </li>
      `;

    const htmlBack = `
        </ul>

    </ul>
</li>                                                                                                        
    `;

    return responseHtml + charges + htmlBack;
  }

  async function createCharge() {
    const chargeName = document.getElementById("charge-name").value;
    const chargeAmount = parseInt(
      document.getElementById("charge-amount").value
    );
    const budgetId = chargeBudgetNames.value;

    Qual.info("Espere porfavor ...", "Estamos cargando sus datos");

    const chargeCreatorResponse = await postData(
      "http://localhost/personal-budget/create/charge/" + budgetId,
      {
        charge: {
          title: chargeName,
          amount: chargeAmount,
        },
      }
    );

    if (chargeCreatorResponse.error) {
      Qual.error("Ocurrio un error", chargeCreatorResponse.error.reason);
      return;
    }

    Qual.success(
      "Cargo agregado correctamente",
      "Se ha agregado un nuevo cargo"
    );

    setTimeout(() => location.reload(), 1500);
  }

  async function deleteCharge(chargeId) {
    selectedChargeId = chargeId;
    Qual.confirm(
      "¿Estas seguro que deseas eliminar este cargo?",
      "",
      inf,
      "OK",
      "Cancel",
      "confirmDeleteCharge",
      "cancelDeleteCharge"
    );
  }

  // EVENTS
  btn_create_budget.addEventListener("click", () => createBudget());
  btn_delete_budget.addEventListener("click", () => deleteBudget());
  btn_update_budget.addEventListener("click", () => updateBudget());
  btn_create_charge.addEventListener("click", () => createCharge());

  function addDeleteChargeButtons() {
    const deleteChargeButtons = document.getElementsByClassName(
      "delete-charge"
    );

    for (let index = 0; index < deleteChargeButtons.length; index++) {
      const deleteChargeButton = deleteChargeButtons[index];
      deleteChargeButton.addEventListener("click", () =>
        deleteCharge(deleteChargeButton.id)
      );
    }
  }

  setTimeout(async () => {
    Qual.info("Espere porfavor ...", "Estamos cargando sus datos");
    await findAll();
    Qual.success(
      "Todo correcto!",
      "Sus datos han sido cargados de forma correcta"
    );

    let budgetCount = 0;

    for (let _budget in budgetsMap) budgetCount++;

    budget_counter.innerHTML = budgetCount;
    remaining_budgets.innerHTML = BUDGET_LIMIT - budgetCount;

    setTimeout(() => {
      addDeleteChargeButtons();
      document.getElementById("closepopup").click();
    }, 1500);
  }, 100);
})();
