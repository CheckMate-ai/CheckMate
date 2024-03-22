import { writable } from 'svelte/store';

export const onboarded = writable<boolean>(false);