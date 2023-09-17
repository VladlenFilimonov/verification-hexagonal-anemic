package com.examples.verification.out.redis.adapter

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.model.VerificationAttempt
import com.examples.verification.domain.model.VerificationAttempts
import com.examples.verification.domain.port.outbound.AcquireDistributedLockPort
import com.examples.verification.domain.port.outbound.ReadVerificationAttemptsPort
import com.examples.verification.domain.port.outbound.SaveVerificationAttemptsPort
import io.kotest.core.spec.style.WordSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.date.shouldBeBetween
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import reactor.test.StepVerifier


@SpringBootTest
@ActiveProfiles("itest")
@ContextConfiguration(classes = [RedisTestApplication::class])
class RedisIntegrationTest(
    readVerificationAttemptsPort: ReadVerificationAttemptsPort,
    saveVerificationAttemptsPort: SaveVerificationAttemptsPort,
    acquireDistributedLockPort: AcquireDistributedLockPort
) : WordSpec() {

    init {
        extension(SpringExtension)

        "Redis Verification Adapter" should {
            val id = UUID.randomUUID()

            "Save verification attempt" {
                val verification = prepareVerification(id)

                StepVerifier.create(saveVerificationAttemptsPort.save(verification))
                    .expectNext(verification)
                    .verifyComplete()
            }

            "Read verification attempt" {
                val expectedAttempt = prepareAttempt(id)

                StepVerifier.create(readVerificationAttemptsPort.read(id))
                    .assertNext { assertAttempt(it, expectedAttempt) }
                    .verifyComplete()
            }
        }

        "Redis Acquire Distributed Lock Adapter" should {
            val id = UUID.randomUUID()
            val cmd = ConfirmVerificationCommand(id, "123", UserInfo("127.0.0.1", "Chrome"))

            "Acquire Lock" {
                StepVerifier.create(acquireDistributedLockPort.acquire(cmd))
                    .expectNext(cmd)
                    .verifyComplete()
            }

            "Not Acquire Lock when it acquired already" {
                StepVerifier.create(acquireDistributedLockPort.acquire(cmd))
                    .expectComplete()
                    .verify()
            }
        }
    }

    //    Docker must already be installed on your system
    companion object {
        init {
            val redis = GenericContainer(DockerImageName.parse("redis:6.0.9-alpine"))
                .withExposedPorts(6379)
            redis.start()
            System.setProperty("spring.data.redis.host", redis.host)
            System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString())
        }
    }
}

private fun assertAttempt(actual: VerificationAttempts, expected: VerificationAttempts) {
    val actualAttempt = actual.attempts[0]
    val expectedAttempt = expected.attempts[0]

    actualAttempt.verificationId.shouldBe(expectedAttempt.verificationId)
    actualAttempt.createdAt.shouldBeBetween(
        expectedAttempt.createdAt.minusSeconds(5L),
        expectedAttempt.createdAt.plusSeconds(5L)
    )
}

private fun prepareAttempt(id: UUID): VerificationAttempts {
    return VerificationAttempts(
        listOf(
            VerificationAttempt(
                id,
                Instant.now().atOffset(ZoneOffset.UTC)
            )
        )
    )
}

private fun prepareVerification(id: UUID): Verification {
    return Verification(
        id = id,
        confirmed = false,
        expired = false,
        code = "123",
        subject = Subject("test@mail.com", SubjectType.EMAIL_CONFIRMATION),
        userInfo = UserInfo("127.0.0.1", "Chrome")
    )
}

