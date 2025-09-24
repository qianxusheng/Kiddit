FROM node:24

WORKDIR /app
RUN npm install -g @angular/cli

COPY package*.json ./
RUN npm install

COPY src/ src/
COPY public/ public/
COPY angular.json ./
COPY tsconfig*.json ./

EXPOSE 4200
CMD ["ng", "serve", "--host", "0.0.0.0"]