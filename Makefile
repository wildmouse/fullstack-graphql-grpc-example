jdb:
	gradle api:jibDockerBuild
dcb:
	docker-compose -f docker/docker-compose-apps.yml build
dcu:
	docker-compose -f docker/docker-compose-apps.yml up
dcd:
	docker-compose -f docker/docker-compose-apps.yml down
mysqlup:
	docker-compose -f docker/docker-compose.yml up
mysqldown:
	docker-compose -f docker/docker-compose.yml down
