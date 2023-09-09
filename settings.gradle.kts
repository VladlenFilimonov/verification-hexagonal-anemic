rootProject.name = "verification-hexagonal-anemic"

include("verification:verification-app")
include("verification:verification-in:verification-rest")
include("verification:verification-domain")
include("verification:verification-out:verification-database")
include("verification:verification-out:verification-cache")
include("verification:verification-out:verification-event")
include("commons")
