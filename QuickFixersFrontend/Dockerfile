FROM node:20-alpine AS build

WORKDIR /app

# The backend URL, passed in when GitHub builds the image
ARG VITE_API_URL=http://localhost:8081/api

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build


FROM nginx:alpine

# Makes Nginx listen on Render's port + keeps page refreshes working
COPY nginx.conf /etc/nginx/templates/default.conf.template

# Serve the dist your build just created
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]