# syntax=docker/dockerfile:1
FROM apache/spark
COPY . .
WORKDIR .
CMD ["./start.bash"]
