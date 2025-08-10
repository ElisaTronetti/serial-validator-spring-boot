terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "eu-central-1"
}

# ECS cluster
resource "aws_ecs_cluster" "main" {
  name = "serial-validator-cluster"
}

# Data source for Amazon Linux 2 ECS-optimized AMI
data "aws_ami" "ecs_ami" {
  most_recent = true
  owners      = ["amazon"]
  filter {
    name   = "name"
    values = ["amzn2-ami-ecs-hvm-*-x86_64-ebs"]
  }
}

# IAM role for ECS EC2 instances
resource "aws_iam_role" "ecs_instance_role" {
  name = "ecs-instance-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = { Service = "ec2.amazonaws.com" },
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_instance_policy" {
  role       = aws_iam_role.ecs_instance_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}

resource "aws_iam_instance_profile" "ecs_instance_profile" {
  name = "ecs-instance-profile"
  role = aws_iam_role.ecs_instance_role.name
}

# Launch configuration to create ECS-capable EC2 instances
resource "aws_launch_configuration" "ecs_launch_config" {
  name_prefix          = "ecs-launch-"
  image_id             = data.aws_ami.ecs_ami.id
  instance_type        = "t3.micro"
  iam_instance_profile = aws_iam_instance_profile.ecs_instance_profile.name

  user_data = <<-EOF
    #!/bin/bash
    echo ECS_CLUSTER=${aws_ecs_cluster.main.name} >> /etc/ecs/ecs.config
  EOF

  lifecycle {
    create_before_destroy = true
  }
}

# Auto Scaling Group to launch 1 EC2 instance in default VPC subnet
resource "aws_autoscaling_group" "ecs_asg" {
  desired_capacity     = 1
  max_size             = 1
  min_size             = 1
  launch_configuration = aws_launch_configuration.ecs_launch_config.name

  # Default VPC subnet:
  vc_zone_identifier = [data.aws_subnet_ids.default.ids[0]]

  tag {
    key                 = "Name"
    value               = "ecs-instance"
    propagate_at_launch = true
  }
}

# Data source for default VPC subnet ids
data "aws_vpc" "default" {
  default = true
}

data "aws_subnet_ids" "default" {
  vpc_id = data.aws_vpc.default.id
}

# ECS task definition with your Docker image
resource "aws_ecs_task_definition" "serial_task" {
  family                   = "serial-validator-task"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([{
    name      = "serial-validator"
    image     = var.docker_image
    essential = true
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
    }]
  }])
}

# ECS service to run your task
resource "aws_ecs_service" "serial_service" {
  name            = "serial-validator-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.serial_task.arn
  desired_count   = 1
  launch_type     = "EC2"
}

variable "docker_image" {
  description = "Docker image URI"
  type        = string
}
