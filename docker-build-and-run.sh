#!/bin/bash

# Exit on error
set -e

# Colors for output
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# Build the application
build_app() {
    echo -e "${GREEN}Building the application...${NC}"
    ./mvnw clean package -DskipTests
    echo -e "${GREEN}Build completed successfully!${NC}"
}

# Build and start the containers
start_containers() {
    echo -e "${GREEN}Starting containers with Docker Compose...${NC}"
    docker-compose up -d --build
    echo -e "${GREEN}Containers are starting up...${NC}"
    echo -e "${GREEN}Application will be available at http://localhost:8080${NC}"
}

# Stop and remove containers
stop_containers() {
    echo -e "${GREEN}Stopping and removing containers...${NC}"
    docker-compose down
    echo -e "${GREEN}Containers stopped and removed.${NC}"
}

# Show logs
show_logs() {
    echo -e "${GREEN}Showing logs (press Ctrl+C to exit)...${NC}"
    docker-compose logs -f
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check the command line argument
case "$1" in
    build)
        build_app
        ;;
    start)
        build_app
        start_containers
        ;;
    stop)
        stop_containers
        ;;
    restart)
        stop_containers
        build_app
        start_containers
        ;;
    logs)
        show_logs
        ;;
    *)
        echo "Usage: $0 {build|start|stop|restart|logs}"
        echo "  build   - Build the application"
        echo "  start   - Build and start the application"
        echo "  stop    - Stop the application"
        echo "  restart - Restart the application"
        echo "  logs    - Show application logs"
        exit 1
        ;;
esac

exit 0
