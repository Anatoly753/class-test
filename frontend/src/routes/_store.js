import { writable } from "svelte/store";

function useLocalStorage(store, itemName) {
  const json = localStorage.getItem(itemName);
  if (json && json !== "undefined") {
    store.set(JSON.parse(json));
  }

  store.subscribe((value) =>
    localStorage.setItem(itemName, JSON.stringify(value))
  );
}

export const user = writable();
if (process.browser) {
  useLocalStorage(user, "user"); //! FIXME: Do not hardcode storage key
}

// export const constructor_answers = writable();
// if (process.browser) {
//   useLocalStorage(constructor_answers, "constructor_answers"); //! FIXME: Do not hardcode storage key
// }

export const solver = writable();
if (process.browser) {
  useLocalStorage(solver, "solver"); //! FIXME: Do not hardcode storage key
}
