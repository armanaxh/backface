## Jenkins

I use Jenkins Configuration as Code Because In this case, we can store configuration in git.

I implement custom-images pipelines and registry for central build tools and images. the product development teams can use these images for their own pipelines.

I implement CI/CD pipelines on the project side. for example, If each project in this repository(each project should be its own repo) is developed and maintained by different teams. these teams can change their own pipeline without any dependency. Just Jenkins maintainer should add their project into configuration.

We have another design with a central configuration and pipeline. It depends on the company. some companies have a central DevOps team and don't want to product development team's deploy separately or change the pipeline.

It's my first Jenkins project. I used Gitlab CI/CD. I was faced with lots of issues.



