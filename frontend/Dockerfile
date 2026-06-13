# ── Stage 1: build ──────────────────────────────────────────────────────────
FROM node:20-alpine AS build

WORKDIR /app

# Cache node_modules separately
COPY package.json package-lock.json ./
RUN npm ci --prefer-offline

COPY . .
RUN npm run build -- --configuration production

# ── Stage 2: serve ───────────────────────────────────────────────────────────
FROM nginx:1.27-alpine

# Remove default static assets
RUN rm -rf /usr/share/nginx/html/*

# Angular outputs to dist/<project-name>/browser (Angular 17+)
# Adjust the source path to match your project name if needed
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Nginx config that supports Angular's HTML5 routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]