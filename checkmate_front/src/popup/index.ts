import App from '../App.svelte'

const target: HTMLElement | null = document.getElementById('app');

async function render() {
  new App({
    target: document.getElementById('app') as HTMLElement,
  }) 
} 

document.addEventListener('DOMContentLoaded', render);
