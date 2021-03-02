<script>
  import {
    Card,
    CardTitle,
    CardSubtitle,
    CardActions,
    Button,
    Icon,
    Divider,
  } from "svelte-materialify/src";
  import { mdiChevronDown } from "@mdi/js";
  import { slide } from "svelte/transition";
  import { onMount } from "svelte";
  import { goto } from "@sapper/app";

  let active = false;
  function toggle() {
    active = !active;
  }

  export let name, title, image;
</script>

<div
  class="wrapper"
  on:click={() => {
    goto(`/tests/${name}`, { scroll: true });
  }}>
  <Card class="ml-3 mr-3 mt-2 mb-2 elevation-4" style="max-width:350px;">
    <img
      src={`/subjects/${name}/${image}-min.jpg`}
      width={350}
      height={230}
      alt="background" />
    <CardTitle>{title}</CardTitle>
    <!-- <CardSubtitle>1,000 miles of wonder</CardSubtitle> -->
    <!-- <CardActions> -->
    <!-- <Button text>Посмотреть</Button> -->
    <!-- <Button text fab size="small" class="ml-auto" on:click={toggle}>
      <Icon path={mdiChevronDown} rotate={active ? 180 : 0} />
    </Button> -->
    <!-- </CardActions> -->
    <!-- {#if active}
    <Divider />
    <div transition:slide class="pl-4 pr-4 pt-2 pb-2">
      Lorem ipsum dolor sit amet consectetur adipisicing elit. Expedita autem,
      asperiores dolores, doloremque ea atque suscipit dolore, ut adipisci amet
      possimus dicta at voluptas consequatur!
    </div>
  {/if} -->
  </Card>
</div>

<style>
  .wrapper {
    transition: all 0.2s ease-in-out;
    cursor: pointer;
  }

  .wrapper:hover {
    transform: scale(1.05);
  }
</style>
