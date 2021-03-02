<script>
  import Page from "../../components/Page.svelte";
  import SubjectCard from "./../../components/SubjectCard.svelte";
  import { Container, Card } from "svelte-materialify/src";
  import { mdiAt } from "@mdi/js";

  let subjects = [
    { name: "mathematics", title: "Метематика", image: "1" },
    { name: "geometry", title: "Геометрия", image: "1" },
    { name: "physics", title: "Физика", image: "1" },
    { name: "russian", title: "Русский язык", image: "1" },
    { name: "english", title: "Английский язык", image: "1" },
    { name: "informatics", title: "Информатика", image: "1" },
    { name: "literature", title: "Литература", image: "1" },
    { name: "history", title: "История", image: "1" },
    { name: "social_studies", title: "Обществознание", image: "1" },
    { name: "chemistry", title: "Химия", image: "1" },
    { name: "biology", title: "Биология", image: "1" },
    { name: "geography", title: "География", image: "1" },
  ];
</script>

<svelte:head>
  <title>Тесты</title>
</svelte:head>

<Page>
  <Container style="max-width:1600px;">
    <Card class="pa-4">
      <h4 class="text-center">Тесты</h4>
      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <div class="d-flex flex-wrap align-content-stretch justify-center">
        {#each subjects as subject, i}
          <!-- <div class="white black-text pa-2 ma-1">Flex Item</div> -->
          <SubjectCard
            name={subject.name}
            title={subject.title}
            image={subject.image} />
        {/each}
      </div>
    </Card>
  </Container>
</Page>
