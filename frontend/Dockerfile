FROM node:current-alpine3.10 as build
LABEL maintainer j.anatoly753@gmail.com
WORKDIR /app/
COPY ./ ./
RUN npm ci
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]

FROM node:current-alpine3.10 as dev
WORKDIR /app/
COPY ./package*.json .
RUN npm ci
EXPOSE 3000
CMD ["npm", "start"]