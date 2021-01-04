
const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const signInMovile = document.getElementById('signInMovile');
const signUpMovile = document.getElementById('signUpMovile');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInMovile.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

signUpMovile.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});