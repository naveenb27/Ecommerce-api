FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install -y
COPY . .
EXPOSE 5173
ENV VITE_BACKEND_URL=http://localhost:8000
CMD ["npm", "run", "dev"]
