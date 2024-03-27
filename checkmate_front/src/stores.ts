import { writable } from 'svelte/store';
import type { IConversation } from './types/types';

export const onboarded = writable<boolean>(false);
export const conversation_details = writable<IConversation|null>(null);