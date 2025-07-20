function displayApps(apps) {
    const container = document.getElementById('app-container');
    container.innerHTML = '';
    apps.forEach((app, index) => {
        const div = document.createElement('div');
        div.className = 'app';
        div.tabIndex = index;
        div.innerHTML = '<img src="' + app.icon + '"><span>' + app.name + '</span>';
        div.onclick = () => {
            document.getElementById('click-sound').play();
        };
        container.appendChild(div);
    });
    const first = container.querySelector('.app');
    if (first) first.focus();
}
