const xRequest = new XMLHttpRequest();

const updatePre = (text) => {
    const preCollection = document.getElementsByTagName('pre');
    for (i = 0; i < preCollection.length; ++i) {
        preCollection[i].innerText = text;
    }
};

xRequest.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
        const jsonObject = JSON.parse(xRequest.responseText);
        let preText = `Now: ${jsonObject.now}\n\n`;
        preText += `$ ${jsonObject.command.commandAndArguments}\n\n`;
        preText += jsonObject.output;
        updatePre(preText);
    }
};

const requestData = (apiPath) => {
    xRequest.open('GET', apiPath, true);
    xRequest.setRequestHeader('Accept', 'application/json');
    xRequest.send();
};

const setTimer = (apiPath) => {
    const checkbox = document.getElementById('autoRefresh');

    setInterval(() => {
        if (checkbox.checked) {
            requestData(apiPath);
        }
    }, 1000);
};

const onload = (commandText, apiPath) => {
    let preText = `Now:\n\n`;
    preText += `$ ${commandText}`;
    updatePre(preText);

    requestData(apiPath);

    setTimer(apiPath);
};
