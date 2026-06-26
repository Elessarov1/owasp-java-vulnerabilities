.PHONY: semgrep semgrep-json semgrep-sarif semgrep-strict semgrep-validate

SEMGREP_IMAGE=semgrep/semgrep
SEMGREP_CONFIG=semgrep/rules
SEMGREP_REPORT_DIR=build/semgrep

semgrep:
	docker run --rm \
		-v "$$(pwd):/src" \
		-w /src \
		$(SEMGREP_IMAGE) \
		semgrep scan --config $(SEMGREP_CONFIG) .

semgrep-json:
	mkdir -p $(SEMGREP_REPORT_DIR)
	docker run --rm \
		-v "$$(pwd):/src" \
		-w /src \
		$(SEMGREP_IMAGE) \
		semgrep scan --config $(SEMGREP_CONFIG) . \
		--json-output=$(SEMGREP_REPORT_DIR)/semgrep.json

semgrep-sarif:
	mkdir -p $(SEMGREP_REPORT_DIR)
	docker run --rm \
		-v "$$(pwd):/src" \
		-w /src \
		$(SEMGREP_IMAGE) \
		semgrep scan --config $(SEMGREP_CONFIG) . \
		--sarif-output=$(SEMGREP_REPORT_DIR)/semgrep.sarif

semgrep-strict:
	docker run --rm \
		-v "$$(pwd):/src" \
		-w /src \
		$(SEMGREP_IMAGE) \
		semgrep scan --config $(SEMGREP_CONFIG) . --error

semgrep-validate:
	docker run --rm \
		-v "$$(pwd):/src" \
		-w /src \
		$(SEMGREP_IMAGE) \
		semgrep scan --config $(SEMGREP_CONFIG) --validate