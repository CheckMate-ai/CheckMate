import Asking from '../Asking.svelte'

const target: HTMLElement | null = document.getElementById('app');

async function render() {
  new Asking({
    target: document.getElementById('app') as HTMLElement,
  }) 
} 

document.addEventListener('DOMContentLoaded', render);
