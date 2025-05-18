APP_NAME = hotela
VERSION := $(shell date +%Y%m%d%H%M%S)
IMAGE_NAME = $(APP_NAME):$(VERSION)
DATABASE_NAME = postgres
NAMESPACE = default
TMP_MANIFEST = k8s/deployment.tmp.yml

deploy: create-secrets deploy-db build
	sed "s|image: $(APP_NAME):.*|image: $(IMAGE_NAME)|" k8s/deployment.yml > $(TMP_MANIFEST)
	kubectl apply -f $(TMP_MANIFEST)
	kubectl rollout status deployment/$(APP_NAME) -n $(NAMESPACE)
	kubectl rollout status deployment/$(DATABASE_NAME) -n $(NAMESPACE)
	rm -f $(TMP_MANIFEST)

deploy-db:
	kubectl apply -f k8s/postgres.yml
	kubectl rollout status deployment/$(DATABASE_NAME) -n $(NAMESPACE)

build:
	gradle build
	docker build -t $(IMAGE_NAME) .
	minikube image load $(IMAGE_NAME)

create-secrets:
	kubectl create secret generic hotela-secret --from-env-file=.env --dry-run=client -o yaml | kubectl apply -f -
	kubectl create secret generic postgres-secret --from-env-file=.env --dry-run=client -o yaml | kubectl apply -f -


restart:
	kubectl rollout restart deployment/$(APP_NAME) -n $(NAMESPACE)
	kubectl rollout status deployment/$(APP_NAME) -n $(NAMESPACE)

restart-db:
	kubectl rollout restart deployment/$(DATABASE_NAME) -n $(NAMESPACE)
	kubectl rollout status deployment/$(DATABASE_NAME) -n $(NAMESPACE)

logs:
	kubectl logs -l app=$(APP_NAME) -n $(NAMESPACE) -f

logs-db:
	kubectl logs -l app=$(DATABASE_NAME) -n $(NAMESPACE) -f

rollback:
	kubectl rollout undo deployment/$(APP_NAME) -n $(NAMESPACE)

rollback-db:
	kubectl rollout undo deployment/$(DATABASE_NAME) -n $(NAMESPACE)

version:
	kubectl get pods -l app=hotela -n default -o jsonpath="{range .items[*]}{.metadata.name}: {.spec.containers[0].image}{'\n'}{end}"

stop:
	kubectl scale deployment/hotela --replicas=0 -n default

stop-db:
	kubectl scale deployment/postgres --replicas=0 -n default

stop-all: stop stop-db

start:
	kubectl scale deployment/hotela --replicas=3 -n default

start-db:
	kubectl scale deployment/postgres --replicas=1 -n default

port-forward-db:
	@if ! grep -q "postgres-service" /etc/hosts; then \
		echo "127.0.0.1 postgres-service" | sudo tee -a /etc/hosts; \
	fi
	@echo "Iniciando port-forward de postgres-service:5432 -> localhost:15432..."
	kubectl port-forward service/postgres-service 15432:5432

start-all: start-db start

.PHONY: build deploy restart redeploy logs rollback-app rollback-db
