FROM node:20-alpine AS build

WORKDIR /app

ARG VITE_API_URL=http://localhost:8081/api

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build


FROM nginx:alpine

ENV PORT=80

COPY nginx.conf /etc/nginx/templates/default.conf.template

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]