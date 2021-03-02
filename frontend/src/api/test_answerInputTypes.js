//TODO: addparams for input and check if it is required

//WARNING:  rightAnswersArray depends from type of input

export let answerInputTypes = [
  {
    name: "Текст",
    type: "text",
    parameters: [
      {
        param: "multiple_lines",
        name: "Несколько строк",
        type: "checkbox",
        default: false,
        required: false,
      },
      // {
      //   param: "length",
      //   name: "Длина (макимальное количество символов для ответа)",
      //   type: "integer",
      //   default: false,
      //   required: false,
      // },
    ],
  },

  {
    name: "Число",
    type: "number",
    parameters: [
      // {
      //   param: "type",
      //   name: "Тип",
      //   type: "enum",
      //   enum: [
      //     { name: "Целое", value: "integer", key: "integer" },
      //     { name: "Дробное", value: "fractional", key: "fractional" },
      //   ], //["integer", "fractional"],
      //   default: { name: "Целое", value: "Целое", key: "integer" },
      //   required: true,
      // },
      // {
      //   param: "with_error", //TODO:
      //   name: "С погрешностью",
      //   type: "checkbox",
      //   default: false,
      //   required: false,
      // },
      // {
      //   param: "min",
      //   name: "Минимальное значение",
      //   type: "integer",
      //   default: false,
      //   required: false,
      // },
      // {
      //   param: "max",
      //   name: "Максимальное значение",
      //   type: "integer",
      //   default: false,
      //   required: false,
      // },
    ],
  },

  {
    name: "Выбор",
    type: "select",
    parameters: [
      {
        param: "random_answers",
        name: "Случайный порядок ответов",
        type: "checkbox",
        default: false,
        required: false,
      },
    ],
  },

  {
    name: "Множественный выбор",
    type: "multipleSelect",
    parameters: [
      {
        param: "random_answers",
        name: "Случайный порядок ответов",
        type: "checkbox",
        default: false,
        required: false,
      },
    ],
  },

  {
    name: "Выбор из нескольких ответов в сетке",
    type: "selectGrid",
    parameters: [
      {
        param: "multiple_lines",
        name: "Случайный порядок строк",
        type: "checkbox",
        default: false,
        required: false,
      },
    ],
  },

  {
    name: "Множественный выбор из нескольких ответов в сетке",
    type: "multipleSelectGrid",
    parameters: [
      {
        param: "multiple_lines",
        name: "Случайный порядок строк",
        type: "checkbox",
        default: false,
        required: false,
      },
    ],
  },

  {
    name: "Выпадающий список",
    type: "dropDownList",
    parameters: [
      {
        param: "multiple_lines",
        name: "Случайный порядок ответов",
        type: "checkbox",
        default: false,
        required: false,
      },
    ],
  },

  {
    name: "Слайдер",
    type: "slider",
    parameters: [
      {
        param: "min",
        name: "Минимальное значение",
        type: "integer",
        default: false,
        required: false,
      },
      {
        param: "max",
        name: "Максимальное значение",
        type: "integer",
        default: false,
        required: false,
      },
      {
        param: "step",
        name: "Шаг",
        type: "integer",
        default: false,
        required: false,
      },
    ],
  },

  {
    name: "Диапазон",
    type: "range",
    parameters: [
      {
        param: "min",
        name: "Минимальное значение",
        type: "integer",
        default: false,
        required: false,
      },
      {
        param: "max",
        name: "Максимальное значение",
        type: "integer",
        default: false,
        required: false,
      },
      {
        param: "step",
        name: "Шаг",
        type: "integer",
        default: false,
        required: false,
      },
    ],
  },

  // { name: "Текст", value: "Текст", key: "string" }, //Одна строка, несколько строк

  // { name: "Число", value: "Число", key: "number" }, //Целое, десятичноеб целое, с погрешностью(checkbox)

  // {
  //   name: "Выбор из нескольких ответов",
  //   value: "Выбор из нескольких ответов",
  //   key: "select",
  // },
  // {
  //   name: "Множественный выбор из нескольких ответов",
  //   value: "Множественный выбор из нескольких ответов",
  //   key: "multipleSelect",
  // },

  // {
  //   name: "Выбор из нескольких ответов (несколько строк, сетка)",
  //   value: "Выбор из нескольких ответов (несколько строк, сетка)",
  //   key: "selectGrid",
  // },
  // {
  //   name: "Множественный выбор из нескольких ответов (несколько строк, сетка)",
  //   value: "Множественный выбор из нескольких ответов (несколько строк, сетка)",
  //   key: "multipleSelectGrid",
  // },

  // {
  //   name: "Выпадающий список",
  //   value: "Выпадающий список",
  //   key: "dropDownList",
  // },

  // {
  //   name: "Шкала",
  //   value: "Шкала",
  //   key: "slider",
  // },

  // { name: "Информатика", value: "Информатика", key: "informatics" },
  // { name: "История", value: "История", key: "history" },
  // { name: "Английский язык", value: "Английский язык", key: "english" },
  // { name: "Литература", value: "Литература", key: "literature" },
  // { name: "Химия", value: "Химия", key: "chemistry" },
  // { name: "Биология", value: "Биология", key: "biology" },
  // { name: "География", value: "География", key: "geography" },
  // { name: "Обществознание", value: "Обществознание", key: "social_studies" },
];

export let subjects = {
  mathematics: "Математика",
  geometry: "Геометрия",
  physics: "Физика",
  russian: "Русский язык",
  informatics: "Информатика",
  history: "История",
  english: "Английский язык",
  literature: "Литература",
  chemistry: "Химия",
  biology: "Биология",
  geography: "География",
  social_studies: "Обществознание",
};
