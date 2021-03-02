<script>
  import { user } from "../routes/_store.js";
  import { cubicInOut } from "svelte/easing";
  let duration = 200;
  let delay = duration;

  const transitionIn = () => ({
    duration,
    delay,
    easing: cubicInOut,
    css: (t) => `opacity: ${t}`,
  });

  const transitionOut = () => ({
    duration,
    delay: 0,
    easing: cubicInOut,
    css: (t) => `opacity: ${t}`,
  });

  export let authenticationRequired = false;
  export let loaded = true;
</script>

<div in:transitionIn out:transitionOut>
  {#if (authenticationRequired === true && $user != null) || authenticationRequired === false}
    {#if loaded}
      <slot />
      <!-- {:else}loading... -->
    {/if}
  {:else}Not allowed!{/if}
</div>
