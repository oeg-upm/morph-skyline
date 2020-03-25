# Derived from official mysql image (our base image)
FROM ubuntu

# Add the content of the sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
WORKDIR /app
COPY . /app
#RUN curl https://www.exasol.com/support/secure/attachment/70216/EXAplus-6.1.0.tar.gz --output EXAplus-6.1.0.tar.gz

#RUN tar -xzf EXAplus-6.1.0.tar.gz

#-c localhost:8899 -u sys -p exasol
RUN apt-get update &&  apt-get install -y openjdk-8-jdk unzip lsof vim net-tools iputils-ping wget mysql-client


# Define default command.
CMD ["tail","-f","/dev/null"]
