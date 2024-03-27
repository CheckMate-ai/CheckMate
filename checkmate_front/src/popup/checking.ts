import Checking from '../pages/Checking.svelte'

const target: HTMLElement | null = document.getElementById('app');

async function render() {
  new Checking({
    target: document.getElementById('app') as HTMLElement,
  }) 
} 

document.addEventListener('DOMContentLoaded', render);
